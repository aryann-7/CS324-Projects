# CS 324 Lab 3 - Observer Design Pattern Explained

## Overview
This solution implements the classic **Observer design pattern** in Java. It models a single piece of state (a `number`) that can be watched by any number of independent listeners. Whenever the state changes, every registered listener is notified automatically — no listener has to poll for changes. This is the foundational pattern behind **publish/subscribe** architectures used throughout distributed systems.

## Architecture Diagram
```
              number (Subject / Publisher)
              extends mySubject
                      │
        notifyObservers() on every setValue()
                      │
        ┌─────────────┴─────────────┐
        ▼                           ▼
   HexNumber                   BinNumber
 (Observer/Subscriber)     (Observer/Subscriber)
   implements myObserver      implements myObserver
        │                           │
   prints hex value            prints binary value
```

```
Lab3.java (Presentation / driver)
        │
        ▼
   number (Subject)  ──notifies──▶  HexNumber, BinNumber (Observers)
```

---

## Solution Files

### 1. myObserver.java (Observer Interface)
Defines the contract every observer must follow.

```java
public interface myObserver {
    public void update();
}
```

**Purpose**: Any class that wants to be notified of changes just implements `update()`. The subject doesn't need to know anything about what a `HexNumber` or `BinNumber` actually does — only that it can call `update()` on it.

---

### 2. mySubject.java (Subject Base Class)
Manages the list of subscribers and the notification mechanism.

```java
import java.util.ArrayList;

public class mySubject {
    private ArrayList<myObserver> observers = new ArrayList<myObserver>();

    public void addObserver(myObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(myObserver obs) {
        observers.remove(obs);
    }

    protected void notifyObservers() {
        for (int i = 0; i < observers.size(); i++)
            observers.get(i).update();
    }
}
```

**Key Points**:
- Keeps a list of every observer that has subscribed via `addObserver()`.
- `notifyObservers()` is `protected` — only subclasses (like `number`) can trigger it, not outside code.
- This class is generic: it has no idea what state it's watching. Any subject that needs pub/sub behavior can just extend it.

---

### 3. number.java (Concrete Subject)
The actual piece of state being observed.

```java
public class number extends mySubject {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int in) {
        value = in;
        notifyObservers();
    }
}
```

**Key Points**:
- Extends `mySubject`, inheriting the subscribe/notify machinery.
- `setValue()` is the only way to change the state, and it always calls `notifyObservers()` right after — so a state change and a notification can never get out of sync.
- Observers read the current value back via `getValue()`.

---

### 4. HexNumber.java / BinNumber.java (Concrete Observers)
Two independent subscribers, each reacting differently to the same event.

```java
public class HexNumber implements myObserver {
    private number n;

    public HexNumber(number in) {
        this.n = in;
        n.addObserver(this);
    }

    public void update() {
        System.out.print(" " + Integer.toHexString(n.getValue()));
    }
}
```

```java
public class BinNumber implements myObserver {
    private number n;

    public BinNumber(number in) {
        this.n = in;
        n.addObserver(this);
    }

    public void update() {
        System.out.print(" " + Integer.toBinaryString(n.getValue()));
    }
}
```

**Key Points**:
- Each observer holds a reference to the subject it's watching (`n`), so it can pull the latest value when notified.
- Subscription happens in the constructor — the moment a `HexNumber` or `BinNumber` is created, it registers itself with `addObserver(this)`. No separate "subscribe" step is needed by the caller.
- `HexNumber` and `BinNumber` know nothing about each other. Adding a third observer (say, `OctalNumber`) requires zero changes to `number`, `mySubject`, or the other observers.

---

### 5. Lab3.java (Driver / Entry Point)
Wires everything together and drives the demo loop.

```java
import java.util.InputMismatchException;
import java.util.Scanner;

public class Lab3 {
    public static void main(String[] args) {
        number n = new number();
        Scanner in = new Scanner(System.in);

        new HexNumber(n);
        new BinNumber(n);

        while (true) {
            try {
                System.out.print("\nEnter a number: ");
                n.setValue(in.nextInt());
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input. Exiting program.");
                in.next();
                break;
            }
        }

        in.close();
    }
}
```

**Key Points**:
- Creates one `number` subject and two observers attached to it.
- Reads integers in a loop; each `setValue()` call fans out to both observers automatically.
- Wrapped in a `try/catch` for `InputMismatchException` so non-integer input exits the program gracefully instead of crashing (this was the Q6 homework extension).

---

## Key Concepts Demonstrated

### 1. **Loose Coupling**
- `number` never imports or references `HexNumber` or `BinNumber` directly — it only knows about the generic `myObserver` interface.
- Observers can be added, removed, or swapped without touching the subject's code.

### 2. **Push-based Notification (vs. Polling)**
- Observers don't ask "has the value changed yet?" in a loop. The subject pushes the update to them the instant it happens, via `notifyObservers()` → `update()`.
- This avoids wasted CPU cycles and reduces the delay between a change and a reaction — critical in distributed systems where "polling" a remote service repeatedly is expensive.

### 3. **One-to-Many Dependency**
- A single subject (`number`) can have any number of dependents (observers) that all react independently to the same event.
- This is exactly the shape of publish/subscribe messaging systems (e.g. Kafka topics, RabbitMQ exchanges): one publisher, many independent subscribers, no subscriber aware of the others.

### 4. **Encapsulation of Reaction Logic**
- Each observer decides for itself *how* to react to an update (`HexNumber` formats as hex, `BinNumber` as binary). The subject doesn't dictate behavior — it just announces "something changed."

---

## Compilation & Execution

### Compile:
```bash
javac src/*.java -d bin
```

### Run:
```bash
java -cp bin Lab3
```

---

## File Summary

| File | Role | Purpose |
|------|------|---------|
| `myObserver.java` | Observer interface | Contract every subscriber must implement (`update()`) |
| `mySubject.java`  | Subject base class | Manages the observer list and broadcasts notifications |
| `number.java`     | Concrete subject | The state being watched; triggers notification on change |
| `HexNumber.java`  | Concrete observer | Subscribes to `number`, prints value in hex |
| `BinNumber.java`  | Concrete observer | Subscribes to `number`, prints value in binary |
| `Lab3.java`       | Driver / entry point | Wires subject + observers together and runs the demo loop |
