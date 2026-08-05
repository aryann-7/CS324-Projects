# CS324 Lab 2 - Architecture Layers Explained

## Overview
This solution implements a classic three-tier architecture in Java. The system is separated into three distinct layers (Presentation, Business, and Data Access), each with a specific responsibility, to manage customer data using a JDBC database connection.

## Architecture Diagram
```
        Presentation Layer (UI, Application_main)
                 │
                 ▼
          Business Layer (BLcustomer)
                 │
                 ▼
        Data Access Layer (DAcustomer, Access_JDBC)
                 │
                 ▼
             Database (UCanAccess)
```

---

## Solution Files

### 1. Application_main.java (Presentation Layer)
Entry point of the application.

```java
package presentation_layer;

public class Application_main {
    public static void main(String[] args) {
        UI ui = new UI();
        ui.print();
    }
}
```

**Purpose**: Starts the application by instantiating the user interface (`UI`) and displaying the menu.

---

### 2. UI.java (Presentation Layer)
Handles user interaction and input/output.

```java
package presentation_layer;

import java.util.Scanner;
import business_layer.BLcustomer;

class UI {
    private BLcustomer cus;
    private final Scanner in;
    // ...
    
    public void add_customer() {
        // ... gets input from user ...
        try {
            cus.add(); // Calls Business Layer
            System.out.println("addition successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // ... other methods (update, delete, view) ...
}
```

**Key Points**:
- Belongs to the `presentation_layer` package.
- Manages the console menu and reads user inputs via `Scanner`.
- Instantiates and communicates **only** with `BLcustomer` (Business Layer).
- Has no direct knowledge of the database or SQL queries.

---

### 3. BLcustomer.java (Business Layer)
Contains the business logic and acts as an intermediary.

```java
package business_layer;

import data_access_layer.DAcustomer;

public class BLcustomer {
    private String cusId;
    private String fName;
    private String lName;
    private DAcustomer cusData;

    public BLcustomer() {
        cusData = new DAcustomer(); // Connects to Data Access Layer
    }

    // ... getters and setters ...

    public void add() throws Exception {
        cusData.add(this);
    }
    // ... other operations (update, delete, getAll) ...
}
```

**Key Points**:
- Belongs to the `business_layer` package.
- Represents a Customer entity with its properties.
- Passes CRUD operation requests from the UI down to the Data Access Layer (`DAcustomer`).
- Keeps the UI completely decoupled from the database implementation.

---

### 4. DAcustomer.java (Data Access Layer)
Handles database CRUD operations specifically for the customer entity.

```java
package data_access_layer;

import business_layer.BLcustomer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAcustomer {
    private Access_JDBC db;

    public void add(BLcustomer cus) throws Exception {
        String sql = "";
        try {
            db.connect();
            Statement s = db.getConnect().createStatement();
            sql = sql + "INSERT INTO customer(id, lname, fname) ";
            sql = sql + "VALUES (" + cus.getCusId() + ",'" + cus.getLName() + "','" + cus.getFName() + "')";
            s.execute(sql);
            db.disconnect();
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }
    // ... other CRUD operations ...
}
```

**Key Points**:
- Belongs to the `data_access_layer` package.
- Contains the SQL statements (INSERT, UPDATE, DELETE, SELECT).
- Uses `Access_JDBC` to establish and close the database connection.
- Receives entity objects from the Business Layer, translates them into SQL, and returns results back (e.g., `List<BLcustomer>`).

---

### 5. Access_JDBC.java (Data Access Layer)
Manages the generic JDBC database connection.

```java
package data_access_layer;

import java.sql.*;

public class Access_JDBC {
    private Connection con;

    public void connect() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection("jdbc:ucanaccess://database//new_test.accdb");
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }
    // ... disconnect() and getters/setters ...
}
```

**Key Points**:
- Connects to a Microsoft Access database using the `UcanaccessDriver`.
- Abstracts the database connection logic away from the specific data operations in `DAcustomer`.

---

## Key Concepts Demonstrated

### 1. **Separation of Concerns**
- Each layer has a singular, distinct responsibility.
- **Presentation** handles UI, **Business** handles logic and data structures, **Data Access** handles persistence.

### 2. **Decoupling**
- The UI layer does not import `java.sql.*`.
- The database logic (like changing from MS Access to MySQL) can be updated without affecting the UI, as long as `BLcustomer`'s interface remains the same.

### 3. **Data Transfer**
- The Business Layer class (`BLcustomer`) is essentially used as a Data Transfer Object to move data between the Presentation and Data Access layers safely.

---

## Compilation & Execution

### Compile:
Ensure all files are compiled from the `src` directory and your JDBC drivers are correctly in your classpath.
```bash
javac src/data_access_layer/*.java src/business_layer/*.java src/presentation_layer/*.java
```

### Run:
```bash
java presentation_layer.Application_main
```

---

## File Summary

| File | Package | Purpose |
|------|---------|---------|
| `Application_main.java` | `presentation_layer` | Application entry point |
| `UI.java`               | `presentation_layer` | Console user interface |
| `BLcustomer.java`       | `business_layer`     | Business logic and entity representation |
| `DAcustomer.java`       | `data_access_layer`  | Database operations for Customer |
| `Access_JDBC.java`      | `data_access_layer`  | Database connection management |
