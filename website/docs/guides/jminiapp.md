---
sidebar_position: 3
---

# JMiniApp

The abstract base class that all applications extend.

**Package:** `com.jminiapp.core.api`

## Understanding the Application Class

`JMiniApp` provides the template method pattern for managing your application's lifecycle. When you extend it, the framework:

1. **Orchestrates the lifecycle** - Calls initialize → run → shutdown in order
2. **Provides context access** - Your app gets state management and file I/O
3. **Handles errors** - Catches exceptions and calls your error handler
4. **Manages startup** - Creates instances and injects dependencies

Your job is simple: implement the three lifecycle methods with your application logic.

## Typical Application Structure

Here's a complete example showing all lifecycle phases:

```java
package com.example.taskmanager;

import com.jminiapp.core.api.*;
import java.util.*;

public class TaskManagerApp extends JMiniApp {
    private List<Task> tasks;
    private Scanner scanner;

    // Required constructor
    public TaskManagerApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        // Phase 1: Setup
        scanner = new Scanner(System.in);

        // Load existing data
        List<Task> data = context.getData();
        tasks = data.isEmpty() ? new ArrayList<>() : new ArrayList<>(data);

        System.out.println("Task Manager started with " + tasks.size() + " tasks");
    }

    @Override
    protected void run() {
        // Phase 2: Main logic
        boolean running = true;

        while (running) {
            System.out.println("\n1. Add Task  2. List Tasks  3. Exit");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Task name: ");
                    String name = scanner.nextLine();
                    tasks.add(new Task(name));
                    System.out.println("Task added!");
                    break;
                case 2:
                    tasks.forEach(task -> System.out.println("- " + task.getName()));
                    break;
                case 3:
                    running = false;
                    break;
            }
        }
    }

    @Override
    protected void shutdown() {
        // Phase 3: Cleanup
        context.setData(tasks);
        scanner.close();
        System.out.println("Tasks saved. Goodbye!");
    }
}
```

## Constructor Requirements

Your application **must** have this exact constructor:

```java
public TaskManagerApp(JMiniAppConfig config) {
    super(config);  // Call parent constructor
}
```

### ✅ Correct Constructor

```java
public class MyApp extends JMiniApp {
    public MyApp(JMiniAppConfig config) {
        super(config);
    }
}
```

### ❌ Incorrect Constructors

```java
// ❌ No constructor - won't compile
public class MyApp extends JMiniApp {
    // Missing constructor!
}

// ❌ Wrong parameters - runtime error
public class MyApp extends JMiniApp {
    public MyApp() {  // Wrong! Needs config parameter
        super(null);
    }
}

// ❌ Doesn't call super - won't work
public class MyApp extends JMiniApp {
    public MyApp(JMiniAppConfig config) {
        // Missing super(config)!
    }
}
```

**Why `super(config)` is required:**
- Passes configuration to framework
- Initializes internal state
- Sets up context access

## The Lifecycle Methods

### initialize()

**When called:** Once at application start, before `run()`

**Purpose:** Set up your application

**Common tasks:**
- Load data from context
- Initialize instance variables
- Set up resources (Scanner, connections, etc.)
- Display welcome messages

```java
@Override
protected void initialize() {
    // Load data
    items = new ArrayList<>(context.getData());

    // Set up resources
    scanner = new Scanner(System.in);

    // Initialize state
    if (items.isEmpty()) {
        items.add(new Item("Welcome"));
    }

    // Welcome message
    System.out.println("App started with " + items.size() + " items");
}
```

**Don't:**
- Perform long-running operations
- Exit the application
- Interact heavily with user (save that for run())

---

### run()

**When called:** After `initialize()` completes

**Purpose:** Execute your main application logic

**Common tasks:**
- Display UI/menu
- Handle user input
- Process data
- Perform computations
- Execute business logic

```java
@Override
protected void run() {
    boolean running = true;

    while (running) {
        // Display menu
        displayMenu();

        // Get input
        int choice = scanner.nextInt();

        // Process
        switch (choice) {
            case 1: addItem(); break;
            case 2: listItems(); break;
            case 3: running = false; break;
        }
    }
}
```

**Don't:**
- Access files directly (use context instead)
- Forget to provide exit mechanism
- Mix UI and business logic too heavily

---

### shutdown()

**When called:** After `run()` completes or on error

**Purpose:** Clean up and save state

**Common tasks:**
- Save data via context
- Export files
- Close resources
- Display goodbye messages
- Generate reports

```java
@Override
protected void shutdown() {
    // Save data
    context.setData(items);

    // Export backup
    try {
        context.exportData("backup.json", "json");
    } catch (IOException e) {
        System.err.println("Backup failed: " + e.getMessage());
    }

    // Clean up
    if (scanner != null) {
        scanner.close();
    }

    // Goodbye
    System.out.println("Data saved. Goodbye!");
}
```

**Don't:**
- Prompt for user input
- Perform complex operations
- Ignore save failures silently

## Error Handling in Practice

Override `handleError()` for custom error handling:

```java
@Override
protected void handleError(Exception e) {
    System.err.println("Error in " + getAppName() + ": " + e.getMessage());
    e.printStackTrace();

    // Specific handling
    if (e instanceof IOException) {
        System.err.println("File operation failed");
        System.err.println("Your data may not be saved");
    } else if (e instanceof IllegalStateException) {
        System.err.println("Invalid application state");
    }

    // Attempt recovery
    try {
        context.exportData("emergency-backup.json", "json");
        System.err.println("Emergency backup created");
    } catch (Exception backupError) {
        System.err.println("Backup also failed");
    }
}
```

**When `handleError()` is called:**
- Exception thrown in `initialize()`
- Exception thrown in `run()`
- Exception thrown in `shutdown()`

**Default behavior:** Prints error message and stack trace

## Application Patterns

### Stateless Application

No persistent data needed:

```java
public class CalculatorApp extends JMiniApp {
    public CalculatorApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        System.out.println("Calculator ready");
    }

    @Override
    protected void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter calculation: ");
        String input = scanner.nextLine();
        // ... calculate ...
        System.out.println("Result: " + result);
    }

    @Override
    protected void shutdown() {
        System.out.println("Goodbye");
        // No data to save
    }
}
```

**Bootstrap:**
```java
JMiniAppRunner.forApp(CalculatorApp.class).run(args);
// No withState() needed
```

---

### Stateful Application

Needs persistent data across runs:

```java
public class NotesApp extends JMiniApp {
    private List<Note> notes;

    public NotesApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        notes = new ArrayList<>(context.getData());
        System.out.println("Loaded " + notes.size() + " notes");
    }

    @Override
    protected void run() {
        // Add/edit notes
        notes.add(new Note("New note"));
    }

    @Override
    protected void shutdown() {
        context.setData(notes);  // Save for next run
    }
}
```

**Bootstrap:**
```java
JMiniAppRunner
    .forApp(NotesApp.class)
    .withState(Note.class)  // Enable state
    .run(args);
```

See [Common Patterns](../getting-started/common-patterns) for more examples.

---

### Interactive vs Batch Processing

**Interactive:**
```java
@Override
protected void run() {
    Scanner scanner = new Scanner(System.in);
    // Loop, get input, respond
    while (running) {
        String command = scanner.nextLine();
        processCommand(command);
    }
}
```

**Batch:**
```java
@Override
protected void run() {
    // Process all data at once
    items.forEach(this::processItem);
    System.out.println("Processed " + items.size() + " items");
}
```

## Access to Framework Services

### Getting the Context

Access context from any lifecycle method:

```java
@Override
protected void initialize() {
    JMiniAppContext ctx = this.context;  // or just: context
    List<Data> data = context.getData();
}
```

### Getting the App Name

```java
@Override
protected void run() {
    String name = getAppName();
    System.out.println("Running: " + name);
}
```

Set via:
```java
JMiniAppRunner
    .forApp(MyApp.class)
    .named("My Application")  // Custom name
    .run(args);
```

Default is class simple name: "MyApp"

## Method Reference

### Constructor

#### `public JMiniApp(JMiniAppConfig config)`
**Required.** Passes config to framework. Must call `super(config)`.

---

### Abstract Methods (You Implement)

#### `protected abstract void initialize()`
Setup phase. Load data, initialize resources.

#### `protected abstract void run()`
Main logic phase. Execute your application.

#### `protected abstract void shutdown()`
Cleanup phase. Save data, close resources.

---

### Protected Methods (You Can Override)

#### `protected void handleError(Exception e)`
Error handler. Override for custom error handling.

**Default:** Prints error message and stack trace.

---

### Public Methods (Framework Uses)

#### `public final void start()`
**Final method.** Called by framework to start lifecycle. Don't override.

#### `public String getAppName()`
Returns the application name.

#### `public JMiniAppContext getContext()`
Returns the context instance.

#### `public void setContext(JMiniAppContext context)`
Sets the context. Framework calls this.

## Common Mistakes

### ❌ Missing Constructor

```java
public class MyApp extends JMiniApp {
    // ❌ No constructor - won't compile!
}
```

### ❌ Not Calling super()

```java
public class MyApp extends JMiniApp {
    public MyApp(JMiniAppConfig config) {
        // ❌ Missing super(config)
    }
}
```

### ❌ Not Implementing All Methods

```java
public class MyApp extends JMiniApp {
    // ❌ Must implement all three: initialize, run, shutdown
}
```

### ❌ Calling start() Yourself

```java
public static void main(String[] args) {
    MyApp app = new MyApp(config);
    app.start();  // ❌ Don't do this! Use JMiniAppRunner
}
```

**Correct:**
```java
public static void main(String[] args) {
    JMiniAppRunner.forApp(MyApp.class).run(args);  // ✅
}
```

## Next Steps

- [Lifecycle Guide](lifecycle) - Deep dive into lifecycle management
- [Context API](context) - Working with state and files
- [JMiniAppRunner](runner) - Bootstrapping your application
- [Build Your First App](../getting-started/build-your-first-app) - Create your first JMiniApp
- [Common Patterns](../getting-started/common-patterns) - Application patterns
