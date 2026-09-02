# CS324 Lab 6 - Java Threads, Concurrency, and Deadlock Explained

## Overview
This laboratory explores concurrent programming in Java, thread synchronization, race condition diagnostics, multi-threaded debugging, and deadlock prevention. The lab consists of three distinct parts:
1. **Part 1 — Analyzing a Faulty Banking Application**: Investigating race conditions and non-deterministic behavior resulting from unsynchronized access to shared mutable state.
2. **Part 2 — Debugging Multithreaded Applications & Deadlocks**: Using debugging tools to inspect threads, monitor locks, and understand the root cause of deadlocks (circular wait on intrinsic locks).
3. **Part 3 — Solving the Banking Problem with Minimal Synchronization**: Designing a robust, minimal solution combining method synchronization (`synchronized`) with deterministic execution coordination (`join()`).

---

## Part 1: Analysis of the Faulty Banking Application

### 1.1 The Scenario
Pete holds two accounts with XBank, starting with $1000 each ($2000 total balance):
- **Checking Account**: Incurs a 10% fee (`-$100`).
- **Savings Account**: Earns a 10% interest (`+$100`).
- **Transfer**: Pete schedules a regular transfer of `$100` from Savings to Checking to balance the accounts.

Pete expects his total balance to remain exactly **$2000**. However, when three threads run concurrently without synchronization, the final total frequently falls short of $2000 (e.g., $1900 or $2090).

### 1.2 Thread Execution Flow (Unsynchronized)
```
          Shared State: savings ($1000), checking ($1000)
                     ┌─────────────────┬─────────────────┐
                     ▼                 ▼                 ▼
             Fee Thread          Interest Thread   Transfer Thread
           (checking -10%)       (savings +10%)    (savings -> checking)
                     │                 │                 │
                     └─────────────────┼─────────────────┘
                                       ▼
                       Race Conditions / Lost Updates
```

### 1.3 Root Cause Analysis

#### A. Race Conditions & Lost Updates (Atomicity Violation)
Methods like `deposit()` and `withdraw()` in `Account.java` perform non-atomic compound operations:
```java
balance += credit; // Read balance -> Add credit -> Write balance
balance -= credit; // Read balance -> Subtract credit -> Write balance
balance *= (100 + rate) / 100.0;
```
When two threads access and mutate `savings.balance` concurrently (e.g., `Interest` and `Transfer`):
1. **Thread A (Interest)** reads `savings.balance` as `1000`.
2. **Thread B (Transfer)** reads `savings.balance` as `1000`.
3. **Thread B (Transfer)** withdraws `100` and writes `savings.balance = 900`.
4. **Thread A (Interest)** computes `1000 * 1.10 = 1100` based on its stale read and writes `savings.balance = 1100`.
5. **Result**: The transfer withdrawal is completely overwritten and lost, or vice-versa.

#### B. Execution Ordering Dependency
- The fee and interest are mathematically calculated based on the initial balance of `$1000` (i.e. $100 fee and $100 interest).
- If the `Transfer` thread runs *before* or *during* the interest calculation, the interest applied to savings is calculated against `$900` ($90 interest) rather than `$1000`, creating a deficit.

#### C. Arbitrary Sleep vs. Coordination
The original code utilized `Thread.sleep(520)` in `main` to wait for background threads. Because thread scheduling is non-deterministic and managed by the operating system scheduler, fixed sleeps offer no execution guarantees and leave race conditions active.

---

## Part 2: Multithreaded Debugging & Deadlock Analysis

### 2.1 The Deadlock Scenario
In the `Deadlock` project, two threads attempt to acquire two shared intrinsic locks (`lock1` and `lock2`) in reverse order:
- **Thread 1**: Acquires `lock1`, then attempts to acquire `lock2`.
- **Thread 2**: Acquires `lock2`, then attempts to acquire `lock1`.

### 2.2 Deadlock Architecture & Circular Wait
```
       ┌───────────┐                    ┌───────────┐
       │  Thread 1 │ ── holds lock 1 ──▶│  Lock 1   │
       └─────┬─────┘                    └─────┬─────┘
             │                                │
        waits for                        held by
             │                                │
             ▼                                ▼
       ┌───────────┐                    ┌───────────┐
       │  Lock 2   │◀── holds lock 2 ───│  Thread 2 │
       └───────────┘                    └───────────┘
```

### 2.3 Debugger Findings (Threads & Monitor State)
Using the debugger and the **Threads** window:
- Both threads enter the `BLOCKED` (or `WAITING`) state.
- **Thread 1** is paused waiting to acquire the monitor for `lock2` while already holding the monitor for `lock1`.
- **Thread 2** is paused waiting to acquire the monitor for `lock1` while already holding the monitor for `lock2`.
- **Conclusion**: Neither thread can proceed, yielding a permanent **deadlock**. The necessary conditions for deadlock (Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait) are all satisfied.

---

## Part 3: Solving the Banking Problem

### 3.1 Solution Strategy: Minimal Synchronization + Join Coordination
To ensure data integrity without unnecessary performance overhead or deadlock risks, the solution applies two targeted techniques:

1. **Intrinsic Method Synchronization (`Account.java`)**:
   Marking mutating operations (`deposit`, `withdraw`, `addinterest`, `getBalance`) as `synchronized`. This ensures that each operation on an account is atomic and mutually exclusive on that account's intrinsic lock (`this`).

2. **Thread Joining for Ordering Guarantees (`Banking.java`)**:
   Using `Thread.join()` to enforce business logic ordering:
   - Fee and Interest threads are started in parallel and joined.
   - The Transfer thread starts only after the initial interest and fees have been fully applied.
   - The main thread joins the Transfer thread before reporting the end-of-month balances.

---

### 3.2 Solution Implementation

#### 1. `Account.java`
```java
package banking;

public class Account {
    final String accountHolder;
    final String accountType;
    double balance = 0;
    
    public Account(String name, String type, double credit) {
        this.accountHolder = name;
        this.accountType = type;        
        this.balance = credit;
    }
    
    public synchronized double getBalance() {
        return balance;
    }
    
    public synchronized void deposit(double credit) {
        balance += credit;
    }
    
    public synchronized void withdraw(double credit) {
        balance -= credit;
    }
    
    public synchronized void addinterest(double rate) {
        balance *= (100 + rate) / 100.0;
    }
}
```

#### 2. `Banking.java`
```java
package banking;

public class Banking {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Application started");
        
        Account savings  = new Account("Pete", "Super Saver", 1000);
        Account checking = new Account("Pete", "Free Checking", 1000);
        
        System.out.println("\nBeginning of month");
        System.out.println(savings.accountType + ":\t" + savings.balance);
        System.out.println(checking.accountType + ":\t" + checking.balance);
        System.out.println("Total before \t" + (checking.balance + savings.balance));
        
        Interest checkInterest = new Interest(checking, -10);        
        Interest saveInterest = new Interest(savings, 10);   
        Transfer transfer = new Transfer(savings, checking, 100);  
              
        // Start interest and fee calculations concurrently
        checkInterest.start();
        saveInterest.start(); 
        
        // Wait for both interest and fee threads to complete
        checkInterest.join();
        saveInterest.join();
        
        // Transfer funds after interest/fees are finalized
        transfer.start();
        transfer.join();
              
        System.out.println("\nEnd of month");
        System.out.println(savings.accountType + ":\t" + savings.balance);
        System.out.println(checking.accountType + ":\t" + checking.balance);
        System.out.println("Total  after \t" + (checking.balance + savings.balance));
        System.out.println("Main thread finished");
    }
}
```

---

## 3.3 Why this Combination is Optimal
- **Minimal Synchronization**: Locking is restricted to individual accounts (`Account` level) rather than locking the entire banking system or using global mutexes.
- **No Deadlock Risk**: Threads do not hold multiple locks across accounts simultaneously.
- **Deterministic & Safe**: Eliminates race conditions, prevents lost updates, and ensures the correct end-of-month balance of exactly **$2000.0** on every execution.
