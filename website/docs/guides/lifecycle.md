---
sidebar_position: 2
---

# Lifecycle Management

JMiniApp provides a well-defined application lifecycle with three phases: initialize, run, and shutdown. Understanding this lifecycle is key to building robust applications.

## The Three Phases

### 1. Initialize Phase

Called once when the application starts. Use this phase to:

- Load existing data from context
- Initialize application state
- Set up resources (connections, files, etc.)
- Validate configuration
- Display welcome messages

```java
@Override
protected void initialize() {
    // Load data
    List<Task> tasks = context.getData();
    
    // Initialize state
    if (tasks.isEmpty()) {
        this.tasks = new ArrayList<>();
        System.out.println("Starting with empty task list");
    } else {
        this.tasks = new ArrayList<>(tasks);
        System.out.println("Loaded " + tasks.size() + " tasks");
    }
    
    // Set up resources
    this.scanner = new Scanner(System.in);
}
```

### 2. Run Phase

The main application logic. This is where your application:

- Displays the user interface
- Handles user input
- Processes data
- Executes business logic
- Performs computations

```java
@Override
protected void run() {
    boolean running = true;
    
    while (running) {
        displayMenu();
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                addTask();
                break;
            case 2:
                listTasks();
                break;
            case 3:
                running = false;
                break;
        }
    }
}
```

### 3. Shutdown Phase

Called when the application exits. Use this phase to:

- Save application state
- Export data to files
- Clean up resources
- Display goodbye messages
- Generate reports

```java
@Override
protected void shutdown() {
    // Save state
    context.setData(tasks);
    
    // Export to file
    try {
        context.exportData("tasks.json", "json");
        System.out.println("Tasks saved successfully");
    } catch (IOException e) {
        System.err.println("Failed to save tasks: " + e.getMessage());
    }
    
    // Clean up
    scanner.close();
    System.out.println("Goodbye!");
}
```

## Lifecycle Flow

```
Application.start()
    │
    ├─→ initialize()
    │   ├─ Load data
    │   ├─ Set up resources
    │   └─ Initialize state
    │
    ├─→ run()
    │   ├─ Main application logic
    │   ├─ User interaction
    │   └─ Data processing
    │
    ├─→ shutdown()
    │   ├─ Save state
    │   ├─ Export data
    │   └─ Clean up
    │
    └─→ handleError(e)  // If exception occurs
```

## Error Handling

The framework automatically catches exceptions and calls `handleError()`:

```java
@Override
protected void handleError(Exception e) {
    System.err.println("Error in " + appName + ": " + e.getMessage());
    e.printStackTrace();
    
    // Custom error handling
    if (e instanceof IOException) {
        System.err.println("File operation failed");
    }
    
    // Attempt recovery
    try {
        context.exportData("backup.json", "json");
    } catch (Exception ex) {
        // Recovery failed
    }
}
```

## Best Practices

### Initialize Phase

DO:
- Load data first
- Initialize all instance variables
- Validate configuration
- Handle missing data gracefully

DON'T:
- Modify user-facing state
- Perform long-running operations
- Exit the application

### Run Phase

DO:
- Keep the main loop clean
- Extract complex logic to methods
- Handle invalid input
- Provide clear feedback

DON'T:
- Access files directly (use context)
- Mix UI and business logic
- Ignore exceptions

### Shutdown Phase

DO:
- Save state before exporting
- Handle export failures
- Clean up resources
- Keep it quick

DON'T:
- Prompt for user input
- Perform complex operations
- Ignore errors silently

## Advanced Patterns

### Graceful Degradation

Handle initialization failures gracefully:

```java
@Override
protected void initialize() {
    try {
        context.importData("data.json", "json");
        tasks = context.getData();
    } catch (IOException e) {
        System.out.println("Could not load data, starting fresh");
        tasks = new ArrayList<>();
    }
}
```

### Resource Management

Properly manage resources across phases:

```java
private Scanner scanner;
private Connection connection;

@Override
protected void initialize() {
    scanner = new Scanner(System.in);
    connection = Database.connect();
}

@Override
protected void shutdown() {
    if (scanner != null) scanner.close();
    if (connection != null) connection.close();
}
```

### State Validation

Validate state before running:

```java
@Override
protected void initialize() {
    tasks = context.getData();
    
    // Validate loaded data
    tasks.removeIf(task -> task == null || !task.isValid());
    
    if (tasks.isEmpty()) {
        System.out.println("No valid tasks found");
    }
}
```

## Common Pitfalls

### Forgetting to Save State

```java
// ❌ Bad: State not saved
@Override
protected void shutdown() {
    System.out.println("Goodbye!");
}

// ✅ Good: State saved before exit
@Override
protected void shutdown() {
    context.setData(tasks);
    System.out.println("Goodbye!");
}
```

### Modifying Context Directly

```java
// ❌ Bad: Direct modification
@Override
protected void run() {
    List<Task> tasks = context.getData();
    tasks.add(new Task());  // Modifies context directly
}

// ✅ Good: Copy, modify, save
@Override
protected void initialize() {
    this.tasks = new ArrayList<>(context.getData());
}

@Override
protected void run() {
    tasks.add(new Task());  // Modifies local copy
}

@Override
protected void shutdown() {
    context.setData(tasks);  // Save back to context
}
```

### Ignoring Exceptions

```java
// ❌ Bad: Silent failure
@Override
protected void shutdown() {
    try {
        context.exportData("json");
    } catch (Exception e) {
        // Ignored
    }
}

// ✅ Good: Handle errors
@Override
protected void shutdown() {
    try {
        context.exportData("json");
        System.out.println("Data saved");
    } catch (Exception e) {
        System.err.println("Save failed: " + e.getMessage());
    }
}
```

## Next Steps

- [Context API](context): Master the context interface
- [JMiniApp](jminiapp): The base application class
- [Format Adapters](adapters): Handle different file formats
