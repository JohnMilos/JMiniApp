---
sidebar_position: 4
---

# JMiniAppContext

The context interface provides your application with access to framework services.

**Package:** `com.jminiapp.core.api`

## Understanding the Context

The context is your gateway to:

- **State Management** - Get, set, and clear your application's data
- **File I/O** - Import and export data in multiple formats
- **Format Registry** - Discover and validate supported formats
- **Import Strategies** - Control how imported data merges with existing state

Access it in your application via `this.context`:

```java
public class MyApp extends JMiniApp {
    @Override
    protected void initialize() {
        List<MyData> data = context.getData();  // ← Access context
    }
}
```

## State Management in Practice

### Loading Data with Fallback

Handle missing or empty data gracefully:

```java
@Override
protected void initialize() {
    List<Task> tasks = context.getData();

    if (tasks.isEmpty()) {
        // First run or no saved data
        tasks = createDefaultTasks();
        System.out.println("Starting with default tasks");
    } else {
        System.out.println("Loaded " + tasks.size() + " tasks");
    }

    this.tasks = new ArrayList<>(tasks);
}

private List<Task> createDefaultTasks() {
    return Arrays.asList(
        new Task("Welcome", "Get started with JMiniApp"),
        new Task("Tutorial", "Complete the quick start guide")
    );
}
```

### Saving Data Conditionally

Only persist if something changed:

```java
public class MyApp extends JMiniApp {
    private List<Item> items;
    private boolean modified = false;

    @Override
    protected void run() {
        // ... user modifies items ...
        items.add(new Item("New item"));
        modified = true;
    }

    @Override
    protected void shutdown() {
        if (modified) {
            context.setData(items);
            System.out.println("Changes saved");
        } else {
            System.out.println("No changes to save");
        }
    }
}
```

### Clearing Data with Confirmation

```java
public void resetApplication() {
    System.out.print("Clear all data? (yes/no): ");
    String response = scanner.nextLine();

    if (response.equalsIgnoreCase("yes")) {
        context.clearData();
        items = new ArrayList<>();
        System.out.println("All data cleared");
    }
}
```

## Import Strategies Decision Guide

When importing data, choose the right strategy for your use case.

### REPLACE (Default)

**Use when:** First-time load, restoring from backup, resetting data

```java
// Replace all existing data
context.importData("backup.json", "json", ImportStrategies.REPLACE);
```

**What happens:**
- Existing data is completely replaced
- New data becomes the entire dataset
- Previous data is lost

**Example scenario:**
```java
// Current: [Task1, Task2]
// Import:  [Task3, Task4]
// Result:  [Task3, Task4]  ← Task1 and Task2 are gone
```

---

### APPEND

**Use when:** Adding new entries, importing logs, merging datasets

```java
// Add imported data to existing data
context.importData("new-tasks.json", "json", ImportStrategies.APPEND);
```

**What happens:**
- Imported data is added to the end
- Existing data is preserved
- May create duplicates

**Example scenario:**
```java
// Current: [Task1, Task2]
// Import:  [Task3, Task4]
// Result:  [Task1, Task2, Task3, Task4]  ← All preserved
```

---

### SKIP_EXISTING

**Use when:** Importing without overwriting, avoiding duplicates

```java
// Only import items that don't already exist
context.importData("data.json", "json", ImportStrategies.SKIP_EXISTING);
```

**What happens:**
- Only new items are imported
- Existing items remain unchanged
- Duplicates are ignored

**Example scenario:**
```java
// Current: [Task1, Task2]
// Import:  [Task2, Task3]  ← Task2 already exists
// Result:  [Task1, Task2, Task3]  ← Imported Task2 skipped, Task3 added
```

---

### MERGE_BY_ID

**Use when:** Syncing changes, updating existing records

```java
// Merge based on unique IDs
ImportStrategy mergeById = new MergeByIdStrategy();
context.importData("updates.json", "json", mergeById);
```

**What happens:**
- Items with same ID are updated
- New items are added
- Unmatched existing items are preserved

**Example scenario:**
```java
// Current: [Task{id:1, done:false}, Task{id:2, done:false}]
// Import:  [Task{id:2, done:true}, Task{id:3, done:false}]
// Result:  [Task{id:1, done:false}, Task{id:2, done:true}, Task{id:3, done:false}]
//          ← Task 2 updated, Task 3 added, Task 1 unchanged
```

### Strategy Decision Tree

```
Need to import data?
│
├─ First time loading / Reset? → REPLACE
│
├─ Adding new entries? → APPEND
│
├─ Avoid duplicates? → SKIP_EXISTING
│
└─ Updating existing records? → MERGE_BY_ID
```

## Working with Multiple Formats

### Import CSV, Export JSON

Real-world scenario: Import spreadsheet data, process it, export as JSON.

```java
@Override
protected void initialize() {
    try {
        // Import from CSV (maybe from user's spreadsheet)
        context.importData("input.csv", "csv");
        items = new ArrayList<>(context.getData());
        System.out.println("Imported " + items.size() + " items from CSV");
    } catch (IOException e) {
        System.err.println("Failed to import CSV: " + e.getMessage());
        items = new ArrayList<>();
    }
}

@Override
protected void shutdown() {
    context.setData(items);

    try {
        // Export as JSON for API consumption
        context.exportData("output.json", "json");
        System.out.println("Exported to JSON");
    } catch (IOException e) {
        System.err.println("Failed to export JSON: " + e.getMessage());
    }
}
```

### Format Detection

Automatically detect file format:

```java
public void importFile(String filePath) {
    try {
        String format = context.detectFormat(filePath);
        System.out.println("Detected format: " + format);

        if (context.supportsFormat(format)) {
            context.importData(filePath, format);
            System.out.println("Import successful");
        } else {
            System.err.println("Format not supported: " + format);
        }
    } catch (Exception e) {
        System.err.println("Import failed: " + e.getMessage());
    }
}
```

### Check Supported Formats

```java
@Override
protected void initialize() {
    List<String> formats = context.getSupportedFormats();
    System.out.println("Supported formats: " + String.join(", ", formats));

    // Example output: "Supported formats: json, csv"
}
```

## Safe Import/Export Patterns

### Import with Error Handling

```java
@Override
protected void initialize() {
    List<Task> tasks;

    try {
        context.importData("tasks.json", "json");
        tasks = context.getData();
        System.out.println("Loaded saved tasks");
    } catch (FileNotFoundException e) {
        System.out.println("No saved data, starting fresh");
        tasks = new ArrayList<>();
    } catch (IOException e) {
        System.err.println("Failed to load data: " + e.getMessage());
        System.out.println("Starting with empty task list");
        tasks = new ArrayList<>();
    }

    this.tasks = tasks;
}
```

### Export with Backup

```java
@Override
protected void shutdown() {
    context.setData(tasks);

    try {
        // Save to primary file
        context.exportData("tasks.json", "json");

        // Also save backup
        context.exportData("tasks-backup.json", "json");

        System.out.println("Data saved successfully");
    } catch (IOException e) {
        System.err.println("Failed to save: " + e.getMessage());
    }
}
```

### Import with Validation

```java
private void importWithValidation(String filePath) {
    try {
        context.importData(filePath, "json");
        List<Task> imported = context.getData();

        // Validate imported data
        if (imported.stream().anyMatch(task -> task == null || !task.isValid())) {
            System.err.println("Invalid data detected, import cancelled");
            context.clearData();
            context.setData(previousTasks);  // Restore previous state
        } else {
            System.out.println("Imported " + imported.size() + " valid tasks");
            this.tasks = new ArrayList<>(imported);
        }
    } catch (IOException e) {
        System.err.println("Import failed: " + e.getMessage());
    }
}
```

## Common Mistakes

### ❌ Forgetting to Call `setData()` Before Shutdown

**Problem:**
```java
@Override
protected void shutdown() {
    System.out.println("Goodbye!");
    // Data not saved! Lost on exit!
}
```

**Fix:**
```java
@Override
protected void shutdown() {
    context.setData(tasks);  // ✅ Save first
    System.out.println("Goodbye!");
}
```

---

### ❌ Modifying `getData()` Result Directly

**Problem:**
```java
@Override
protected void run() {
    List<Task> tasks = context.getData();
    tasks.add(new Task());  // ❌ Modifies internal state directly
}
```

**Why it's bad:** Changes internal framework state unpredictably

**Fix:**
```java
@Override
protected void initialize() {
    this.tasks = new ArrayList<>(context.getData());  // ✅ Copy
}

@Override
protected void run() {
    tasks.add(new Task());  // ✅ Modify local copy
}

@Override
protected void shutdown() {
    context.setData(tasks);  // ✅ Save back
}
```

---

### ❌ Not Handling Import Exceptions

**Problem:**
```java
@Override
protected void initialize() {
    context.importData("data.json", "json");  // ❌ Throws IOException
    tasks = context.getData();
}
```

**What happens:** App crashes if file doesn't exist or is corrupted

**Fix:**
```java
@Override
protected void initialize() {
    try {
        context.importData("data.json", "json");
        tasks = new ArrayList<>(context.getData());
    } catch (IOException e) {
        System.out.println("Starting fresh: " + e.getMessage());
        tasks = new ArrayList<>();
    }
}
```

## API Reference

### State Management

#### `<T> List<T> getData()`
Returns the current application state as a typed list.

#### `<T> void setData(List<T> data)`
Sets the application state. Call before shutdown to persist changes.

#### `void clearData()`
Removes all application state.

---

### Import Operations

#### `void importData(String format) throws IOException`
Imports from default location using specified format.

#### `void importData(String filePath, String format) throws IOException`
Imports from specific file path.

#### `void importData(String format, ImportStrategy strategy) throws IOException`
Imports with custom merge strategy.

#### `void importData(String filePath, String format, ImportStrategy strategy) throws IOException`
Imports from path with custom strategy.

---

### Export Operations

#### `void exportData(String format) throws IOException`
Exports to default location.

#### `void exportData(String filePath, String format) throws IOException`
Exports to specific file path.

---

### Format Operations

#### `List<String> getSupportedFormats()`
Returns list of registered format names.

#### `boolean supportsFormat(String format)`
Checks if format is supported.

#### `String detectFormat(String filePath)`
Detects format from file extension.

## Next Steps

- [Import Strategies](import-strategies) - Detailed strategy documentation
- [Format Adapters](adapters) - Working with different file formats
- [Lifecycle](lifecycle) - When to use context methods
- [JMiniAppRunner](runner) - Configuring adapters and state
