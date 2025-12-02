---
sidebar_position: 1
---

# Introduction

Welcome to **JMiniApp**, a lightweight educational Java framework designed for building standalone mini applications with seamless and flexible state persistence using multiple file formats.

## What is JMiniApp?

JMiniApp is an educational Java framework that simplifies the development of standalone mini applications by providing:

- **Seamless State Persistence**: Effortless state management with automatic persistence across sessions using file-based storage
- **Multi-Format Support**: Built-in support for importing and exporting data in multiple formats (JSON, CSV, and more)
- **Clean Lifecycle Management**: Well-defined phases (initialize, run, shutdown) that separate concerns and promote good architecture
- **Extensible Architecture**: Easy-to-implement adapters for custom data formats
- **Zero External Dependencies**: Minimal footprint with no external dependencies required for core functionality

## Why JMiniApp?

### Simple and Intuitive

JMiniApp follows a straightforward template pattern that makes it easy to understand and quick to learn. You focus on your application logic while the framework handles the infrastructure.

```java
public class MyApp extends JMiniApp {
    protected void initialize() {
        // Set up your app
    }

    protected void run() {
        // Your main application logic
    }

    protected void shutdown() {
        // Clean up resources
    }
}
```

### Seamless State Persistence

Manage your application state with a simple, type-safe API and automatic persistence:

```java
// Get current state
List<Todo> todos = context.getData();

// Update state (persisted automatically on shutdown)
context.setData(updatedTodos);

// Clear all state
context.clearData();
```

### Flexible File Operations

Import and export data in multiple formats with a unified API:

```java
// Export to JSON
context.exportData("todos.json", "json");

// Import from CSV
context.importData("todos.csv", "csv");

// Auto-detect format
String format = context.detectFormat("data.json");
```

### Built for Extensibility

Create custom format adapters to support any data format your application needs:

```java
public class MyCustomAdapter implements JMiniFormatAdapter<MyModel> {
    public String getFormatName() {
        return "custom";
    }

    public List<MyModel> read(InputStream input) {
        // Your custom deserialization logic
    }

    public void write(List<MyModel> data, OutputStream output) {
        // Your custom serialization logic
    }
}
```

## Key Features

### Lifecycle Management
Well-defined application lifecycle with initialize, run, and shutdown phases. The framework handles the orchestration and error management automatically.

### Import Strategies
Control how imported data merges with existing state:
- **Replace**: Replace all existing data (default)
- **Append**: Add imported data to existing data
- **Merge by ID**: Smart merging based on unique identifiers
- **Skip Existing**: Only import new items

### Format Detection
Automatic format detection based on file extensions or content analysis, making file operations more convenient.

### Resource Path Management
Configurable resource paths following Maven conventions, with support for both relative and absolute file paths.

## Who Should Use JMiniApp?

JMiniApp is perfect for:

- **Learning Projects**: Educational tool for understanding Java application architecture and design patterns
- **Console Applications**: Interactive command-line tools with persistent state across sessions
- **Standalone Utilities**: Small tools that need simple state management without database complexity
- **Prototypes and Demos**: Quick application development to demonstrate concepts
- **Data Processing Scripts**: ETL applications that work with multiple file formats

## What's Next?

Ready to get started? Check out these resources:

- [Installation](getting-started/installation): Set up JMiniApp in your project
- [Build Your First App](getting-started/build-your-first-app): Create your first JMiniApp in 5 minutes
- [Lifecycle Guide](guides/lifecycle): Master the application lifecycle
- [Templates](examples/templates): Application templates and examples
