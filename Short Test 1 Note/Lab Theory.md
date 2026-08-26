# CS324 Labs 2 and 3: Software Architectures

## Overview

Labs 2 and 3 demonstrate two different ways of organizing software:

- **Lab 2:** Layered Architecture
- **Lab 3:** Publish-Subscribe Architecture, implemented with the Observer design pattern

Neither lab represents Microservices Architecture. Lab 2 is a single application divided into internal layers, while Lab 3 is a single Java program in which one object notifies several listeners.

## Lab 2: Layered Architecture

Lab 2 implements a classic three-layer architecture for managing customer data. Each layer has a separate responsibility and communicates with the layer directly below it.

```text
Presentation Layer
        |
        v
Business Layer
        |
        v
Data Access Layer
        |
        v
Database
```

### Presentation Layer

`Application_main` starts the program, and `UI` handles the console menu, user input, and output. The UI communicates with `BLcustomer` instead of accessing the database directly.

### Business Layer

`BLcustomer` represents customer information and forwards operations such as add, update, delete, and retrieve. This layer acts as an intermediary between the user interface and the persistence code.

### Data Access Layer

`DAcustomer` contains the customer-related database operations and SQL statements. `Access_JDBC` manages the JDBC connection to the Microsoft Access database.

### Why this is Layered Architecture

The application separates presentation, business, and data concerns. A change to the database implementation can be made mostly within the data access layer without requiring changes to the user interface. Likewise, the UI does not need to know how SQL queries or JDBC connections work.

The main characteristics shown by Lab 2 are:

- Separation of concerns
- One-way communication between layers
- Encapsulation of database details
- Reduced coupling between the UI and persistence code

## Lab 3: Publish-Subscribe Architecture

Lab 3 implements the Observer pattern, which has the same basic communication shape as a publish-subscribe system: one publisher announces a change, and multiple independent subscribers react to it.

```text
number (Subject / Publisher)
          |
          | notifies
          +----------> HexNumber (Observer / Subscriber)
          |
          +----------> BinNumber (Observer / Subscriber)
```

### Publisher

`number` stores the current integer value. When `setValue()` changes the value, it calls `notifyObservers()`.

### Subscription Manager

`mySubject` stores a list of objects implementing `myObserver`. It provides `addObserver()` and `removeObserver()` for subscription management, and it broadcasts updates to all registered observers.

### Subscribers

`HexNumber` and `BinNumber` implement `myObserver`. Each subscribes to the `number` object and defines its own `update()` behavior:

- `HexNumber` displays the value in hexadecimal.
- `BinNumber` displays the value in binary.

The subject does not need to know the concrete observer types. A new observer could be added without modifying the existing subject or subscribers.

### Why this is Publish-Subscribe Architecture

The publisher and subscribers are loosely coupled. The publisher only depends on the observer interface, while each subscriber independently decides how to respond to an update. One state change is distributed to many listeners, which is the core publish-subscribe relationship.

Lab 3 is a small in-memory example rather than a distributed messaging system. Real publish-subscribe systems commonly use brokers, topics, or event queues, but the communication principle is the same.

## Why These Labs Are Not Microservices Architecture

Microservices Architecture divides an application into independently deployable services. Each service usually owns a focused business capability and communicates with other services over a network, often through HTTP or messaging.

The projects in Labs 2 and 3 do not show this structure:

- Lab 2 contains one application with internal layers, not independently deployable services.
- Lab 3 contains one Java process with objects communicating in memory, not networked services.

Therefore, the architecture mapping is:

| Lab | Architecture represented |
| --- | --- |
| Lab 2 | Layered Architecture |
| Lab 3 | Publish-Subscribe Architecture |
| Labs 2 and 3 | No Microservices Architecture |
