# CS324 Lab Week 2 - Explained

## Overview
This solution implements a polymorphic music system using inheritance and abstract classes in Java. The system allows different instruments (Flute, Guitar) to play musical notes.

## Class Diagram
```
        Instrument          Note
            △                
            │          
        ┌───┴───┐
      Flute    Guitar
      
    Music (uses Instrument)
```

---

## Solution Files

### 1. Note.java
Enum that represents musical notes.

```java
/**
 * Note enum representing musical notes
 */
public enum Note {
    MIDDLE_C("Middle C"),
    C_SHARP("C#");
    
    private String description;
    
    Note(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
```

**Purpose**: Defines the notes that instruments can play.

---

### 2. Instrument.java (Question 1 & 2)
Abstract base class defining the instrument interface.

```java
/**
 * Abstract Instrument class
 * Base class for all instruments
 */
public abstract class Instrument {
    
    /**
     * Abstract method to play a note
     * @param n the Note to play
     */
    public abstract void play(Note n);
}
```

**Key Points**:
- Abstract class that cannot be instantiated directly
- Defines contract for all subclasses via abstract `play(Note n)` method
- Each subclass must implement the play method

---

### 3. Flute.java (Question 1 & 2)
Concrete implementation of Instrument for a flute.

```java
/**
 * Flute class - inherits from Instrument
 * Implements the play method for a flute
 */
public class Flute extends Instrument {
    
    /**
     * Override play method to print flute playing message
     * @param n the Note to play
     */
    @Override
    public void play(Note n) {
        System.out.println("Flute is playing note " + n);
    }
}
```

**Key Points**:
- Extends `Instrument` class
- Implements the abstract `play()` method
- Uses `@Override` annotation to indicate method overriding
- Prints: "Flute is playing note [note]"

---

### 4. Guitar.java (Question 1 & 2)
Concrete implementation of Instrument for a guitar.

```java
/**
 * Guitar class - inherits from Instrument
 * Implements the play method for a guitar
 */
public class Guitar extends Instrument {
    
    /**
     * Override play method to print guitar playing message
     * @param n the Note to play
     */
    @Override
    public void play(Note n) {
        System.out.println("Guitar is playing note " + n);
    }
}
```

**Key Points**:
- Extends `Instrument` class
- Implements the abstract `play()` method
- Uses `@Override` annotation to indicate method overriding
- Prints: "Guitar is playing note [note]"

---

### 5. Music.java (Question 3 & 4)
Main class demonstrating polymorphism.

```java
/**
 * Music class - demonstrates polymorphism
 * Implements the tune method that works with any Instrument type
 */
public class Music {
    
    /**
     * Polymorphic method that plays any instrument
     * Determines the correct object type at runtime and calls its play method
     * @param instrument the Instrument to play
     * @param note the Note to play on the instrument
     */
    public void tune(Instrument instrument, Note note) {
        instrument.play(note);
    }
    
    /**
     * Main method to test the program
     */
    public static void main(String[] args) {
        // Create Music instance
        Music music = new Music();
        
        // Create different instruments
        Instrument flute = new Flute();
        Instrument guitar = new Guitar();
        
        // Play different notes on different instruments
        // Test with Flute
        System.out.println("--- Testing Flute ---");
        music.tune(flute, Note.MIDDLE_C);
        music.tune(flute, Note.C_SHARP);
        
        // Test with Guitar
        System.out.println("\n--- Testing Guitar ---");
        music.tune(guitar, Note.MIDDLE_C);
        music.tune(guitar, Note.C_SHARP);
    }
}
```

**Key Points**:
- **Polymorphic `tune()` method**: Takes any `Instrument` type as parameter
- **Runtime type determination**: Java's polymorphism determines at runtime which `play()` method to call
- **Main method**: Tests the system with both Flute and Guitar objects

**Expected Output**:
```
--- Testing Flute ---
Flute is playing note Middle C
Flute is playing note C#

--- Testing Guitar ---
Guitar is playing note Middle C
Guitar is playing note C#
```

---

## Question 5 (Homework): Convert Instrument to Interface

For the homework, convert the abstract class to an interface:

```java
/**
 * Instrument interface
 * Defines the contract for all playable instruments
 */
public interface Instrument {
    
    /**
     * Play a musical note
     * @param n the Note to play
     */
    void play(Note n);
}
```

Then update Flute and Guitar to implement the interface:

```java
// Flute.java
public class Flute implements Instrument {
    @Override
    public void play(Note n) {
        System.out.println("Flute is playing note " + n);
    }
}

// Guitar.java
public class Guitar implements Instrument {
    @Override
    public void play(Note n) {
        System.out.println("Guitar is playing note " + n);
    }
}
```

The Music class remains the same since Java's polymorphism works equally well with interfaces.

---

## Key Concepts Demonstrated

### 1. **Inheritance**
- Flute and Guitar extend Instrument
- Both inherit the contract of the Instrument class

### 2. **Polymorphism**
- The `tune()` method accepts any Instrument type
- At runtime, Java determines which `play()` method to call based on the actual object type
- Same method call produces different results depending on object type

### 3. **Abstract Classes**
- Instrument defines abstract method `play(Note n)`
- Subclasses must provide concrete implementations

### 4. **Encapsulation**
- Each class has a single, well-defined responsibility
- Methods handle specific behaviors (Flute plays like a flute, etc.)

### 5. **Method Overriding**
- `@Override` annotation ensures we're correctly overriding parent class methods
- Each instrument provides its own implementation of `play()`

---

## Compilation & Execution

### Compile:
```bash
javac *.java
```

### Run:
```bash
java Music
```

---

## File Summary

| File              | Purpose                      | Questions |
|-------------------|------------------------------|-----------|
| Note.java         | Define musical notes         | Setup     |
| Instrument.java   | Abstract base class          | 1, 2      |
| Flute.java        | Flute implementation         | 1, 2      |
| Guitar.java       | Guitar implementation        | 1, 2      |
| Music.java        | Polymorphic demonstration    | 3, 4      |