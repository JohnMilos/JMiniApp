---
sidebar_position: 5
---

# JMiniAppRunner

The fluent builder API for bootstrapping and launching applications.

**Package:** `com.jminiapp.core.engine`

## The Bootstrap Process

`JMiniAppRunner` is your application's entry point. It:

1. **Builds the configuration** from your builder calls
2. **Creates your app instance** via reflection
3. **Initializes the context** with state and adapters
4. **Starts the lifecycle** (initialize → run → shutdown)
5. **Handles errors** automatically

Think of it as the "main method" setup for your JMiniApp.

## Minimal Configuration

The simplest possible setup:

```java
public static void main(String[] args) {
    JMiniAppRunner
        .forApp(MyApp.class)
        .run(args);
}
```

**When to use:** Stateless applications that don't need data persistence.

**What happens:**
- App runs with default settings
- No state management (getData() returns empty list)
- No file I/O capabilities
- App name defaults to "MyApp"

## Adding State Management

Enable persistent state:

```java
public static void main(String[] args) {
    JMiniAppRunner
        .forApp(MyApp.class)
        .withState(MyData.class)  // ← Enables state persistence
        .run(args);
}
```

**Why you need `withState()`:**
- Enables type-safe `context.getData()` and `context.setData()`
- Framework knows what type to expect
- Required for state persistence

**Type safety:** The class you pass to `withState()` must match the type you use in your app:

```java
// In your app
List<MyData> data = context.getData();  // Must match withState(MyData.class)
```

## Registering Format Adapters

Enable file import/export:

```java
public static void main(String[] args) {
    JMiniAppRunner
        .forApp(MyApp.class)
        .withState(MyData.class)
        .withAdapters(new MyJSONAdapter())  // ← Enable JSON I/O
        .run(args);
}
```

**What this enables:**
- `context.exportData("data.json", "json")` - Save to JSON
- `context.importData("data.json", "json")` - Load from JSON
- Format detection and validation

### Multiple Adapters

Support multiple formats:

```java
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(MyData.class)
    .withAdapters(
        new MyJSONAdapter(),  // Support JSON
        new MyCSVAdapter()    // Support CSV
    )
    .run(args);
```

Now your app can work with both JSON and CSV files!

See the [Format Adapters](adapters) guide for more details.

## Custom Resource Paths

Control where files are saved/loaded:

```java
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(MyData.class)
    .withResourcesPath("data/")  // ← Custom location
    .run(args);
```

**Default location:** `src/main/resources/`

**Path types:**
- **Relative:** `"data/"` - relative to working directory
- **Absolute:** `"/Users/name/app-data/"` - full path

**When to customize:**
- Production deployments (outside project structure)
- Multiple environments (dev/test/prod)
- User-specific data directories

## Naming Your Application

Give your app a custom name:

```java
JMiniAppRunner
    .forApp(MyApp.class)
    .named("Task Manager")  // ← Custom display name
    .withState(Task.class)
    .run(args);
```

**Used for:**
- Log messages
- Error reporting
- Display purposes

**Default:** Simple class name (e.g., "MyApp")

## Full Configuration Example

Putting it all together:

```java
public static void main(String[] args) {
    JMiniAppRunner
        .forApp(TaskManagerApp.class)
        .named("Task Manager v1.0")
        .withState(Task.class)
        .withResourcesPath("user-data/")
        .withAdapters(
            new TaskJSONAdapter(),
            new TaskCSVAdapter()
        )
        .run(args);
}
```

This configuration:
- ✅ Runs `TaskManagerApp`
- ✅ Uses "Task Manager v1.0" as the app name
- ✅ Manages `Task` objects as state
- ✅ Stores files in `user-data/` directory
- ✅ Supports both JSON and CSV formats

## Static Helper Method

Quick shortcut for minimal setup:

```java
public static void main(String[] args) {
    JMiniAppRunner.run(MyApp.class, args);
}
```

Equivalent to:
```java
JMiniAppRunner.forApp(MyApp.class).run(args);
```

## Configuration Mistakes & Fixes

### ❌ Forgot `withState()`

**Problem:**
```java
JMiniAppRunner
    .forApp(MyApp.class)
    .run(args);  // No withState()

// In your app
List<MyData> data = context.getData();  // Returns empty list always!
context.setData(data);  // Does nothing!
```

**Fix:**
```java
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(MyData.class)  // ✅ Add this
    .run(args);
```

---

### ❌ Wrong Adapter Type

**Problem:**
```java
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(Task.class)
    .withAdapters(new TodoJSONAdapter())  // Wrong! TodoJSONAdapter<Todo>, not <Task>
    .run(args);
```

**Error:** `ClassCastException` when importing/exporting

**Fix:**
```java
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(Task.class)
    .withAdapters(new TaskJSONAdapter())  // ✅ Matches Task.class
    .run(args);
```

---

### ❌ Adapter Not Registered

**Problem:**
```java
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(MyData.class)
    .run(args);  // No adapters registered

// In your app
context.exportData("data.json", "json");  // ⚠️ Unsupported format: json
```

**Fix:**
```java
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(MyData.class)
    .withAdapters(new MyJSONAdapter())  // ✅ Register JSON adapter
    .run(args);
```

---

### ❌ Multiple `withState()` Calls

**Problem:**
```java
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(OldData.class)
    .withState(NewData.class)  // Last one wins!
    .run(args);
```

**Result:** Only `NewData.class` is used, `OldData.class` is ignored.

**Fix:** Only call `withState()` once with the correct type.

## Builder Method Reference

### `forApp(Class<? extends JMiniApp> appClass)`
**Required.** Specifies your application class. Must be called first.

### `withState(Class<?> stateClass)`
**Optional.** Enables state persistence for the given type.

### `named(String appName)`
**Optional.** Sets a custom application name (default: class simple name).

### `withResourcesPath(String resourcesPath)`
**Optional.** Sets where files are stored (default: `src/main/resources/`).

### `withAdapters(JMiniFormatAdapter<?>... adapters)`
**Optional.** Registers format adapters for import/export (default: none).

### `run(String[] args)`
**Required.** Builds config, creates app, and starts lifecycle. Call this last.

## Next Steps

- [JMiniApp](jminiapp) - Understanding the application class
- [JMiniAppContext](context) - Working with state and files
- [JMiniAppConfig](config) - Configuration details
- [Format Adapters](adapters) - File format support
- [Build Your First App](../getting-started/build-your-first-app) - Create your first JMiniApp
