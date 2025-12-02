---
sidebar_position: 7
---

# Format Adapters

Pluggable components that handle serialization and deserialization for different file formats.

**Package:** `com.jminiapp.core.api`

## What are Format Adapters?

Format adapters translate between your Java objects and file formats (JSON, CSV, XML, etc.). They enable:

- **Export data** - Convert objects to files
- **Import data** - Load files back to objects
- **Format flexibility** - Support multiple formats in one app
- **Custom formats** - Create adapters for any format

Without adapters, your app has no file I/O capabilities.

## Built-in Adapters

JMiniApp includes two built-in adapters.

### JSONAdapter

For JSON file format - ideal for configuration files, API data, and simple storage.

**Features:**
- Human-readable format
- Widely supported
- Good for structured data
- Easy to edit manually

**Usage:**
```java
import com.jminiapp.core.adapters.JSONAdapter;

public class TaskJSONAdapter extends JSONAdapter<Task> {
    public TaskJSONAdapter() {
        super(Task.class);
    }
}
```

**Example output:**
```json
[
  {
    "id": 1,
    "name": "Buy groceries",
    "done": false
  },
  {
    "id": 2,
    "name": "Write documentation",
    "done": true
  }
]
```

---

### CSVAdapter

For CSV (Comma-Separated Values) format - ideal for spreadsheet integration and data exchange.

**Features:**
- Excel/Google Sheets compatible
- Good for tabular data
- Easy data analysis
- Simple format

**Usage:**
```java
import com.jminiapp.core.adapters.CSVAdapter;

public class TaskCSVAdapter extends CSVAdapter<Task> {
    public TaskCSVAdapter() {
        super(Task.class);
    }
}
```

**Example output:**
```csv
id,name,done
1,Buy groceries,false
2,Write documentation,true
```

## Choosing the Right Adapter

| Format | Best For | Pros | Cons |
|--------|----------|------|------|
| **JSON** | Configuration files, API data, nested structures | Human-readable, widely supported, handles complex objects | Slightly verbose |
| **CSV** | Tabular data, spreadsheet exchange, reports | Excel compatible, compact, easy analysis | Limited to flat data |

**Decision guide:**

- **Need to edit in Excel?** → CSV
- **Have nested objects?** → JSON
- **Exchanging with APIs?** → JSON
- **Simple table data?** → CSV
- **Both?** → Register both adapters!

## Registering Adapters

Adapters must be registered with `JMiniAppRunner`:

### Single Adapter

```java
public static void main(String[] args) {
    JMiniAppRunner
        .forApp(MyApp.class)
        .withState(Task.class)
        .withAdapters(new TaskJSONAdapter())  // ← Register adapter
        .run(args);
}
```

Now you can use:
```java
context.exportData("tasks.json", "json");
context.importData("tasks.json", "json");
```

### Multiple Adapters

Support both JSON and CSV:

```java
public static void main(String[] args) {
    JMiniAppRunner
        .forApp(MyApp.class)
        .withState(Task.class)
        .withAdapters(
            new TaskJSONAdapter(),  // ← JSON support
            new TaskCSVAdapter()    // ← CSV support
        )
        .run(args);
}
```

Now you can use:
```java
// Export to different formats
context.exportData("tasks.json", "json");
context.exportData("tasks.csv", "csv");

// Import from either format
context.importData("input.json", "json");
context.importData("input.csv", "csv");
```

## Using Adapters in Your App

### Basic Export

```java
@Override
protected void shutdown() {
    context.setData(tasks);

    try {
        context.exportData("tasks.json", "json");
        System.out.println("Tasks saved to tasks.json");
    } catch (IOException e) {
        System.err.println("Export failed: " + e.getMessage());
    }
}
```

### Basic Import

```java
@Override
protected void initialize() {
    try {
        context.importData("tasks.json", "json");
        tasks = new ArrayList<>(context.getData());
        System.out.println("Loaded " + tasks.size() + " tasks");
    } catch (IOException e) {
        System.out.println("Starting with empty task list");
        tasks = new ArrayList<>();
    }
}
```

### Multi-Format Export

```java
@Override
protected void shutdown() {
    context.setData(tasks);

    try {
        // Save as JSON for the app
        context.exportData("tasks.json", "json");

        // Also export as CSV for spreadsheet analysis
        context.exportData("tasks-export.csv", "csv");

        System.out.println("Saved in both JSON and CSV formats");
    } catch (IOException e) {
        System.err.println("Export failed: " + e.getMessage());
    }
}
```

## Real-World Scenario: Import CSV, Export JSON

Import data from a spreadsheet, process it, and export as JSON for an API.

```java
public class DataConverterApp extends JMiniApp {
    private List<Record> records;

    public DataConverterApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        try {
            // Import from CSV (maybe exported from Excel)
            context.importData("input.csv", "csv");
            records = new ArrayList<>(context.getData());
            System.out.println("Imported " + records.size() + " records from CSV");
        } catch (IOException e) {
            System.err.println("Failed to import CSV: " + e.getMessage());
            records = new ArrayList<>();
        }
    }

    @Override
    protected void run() {
        // Process records (filter, transform, validate)
        records = records.stream()
            .filter(Record::isValid)
            .map(this::enrichRecord)
            .collect(Collectors.toList());

        System.out.println("Processed " + records.size() + " valid records");
    }

    @Override
    protected void shutdown() {
        context.setData(records);

        try {
            // Export as JSON for API consumption
            context.exportData("output.json", "json");
            System.out.println("Exported to JSON successfully");
        } catch (IOException e) {
            System.err.println("Failed to export JSON: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(DataConverterApp.class)
            .withState(Record.class)
            .withAdapters(
                new RecordCSVAdapter(),   // For import
                new RecordJSONAdapter()   // For export
            )
            .run(args);
    }
}
```

## Adapter Lifecycle

Understanding when adapters are called:

### read() - Import Operation

Called by `context.importData()`:

```java
// Your app
context.importData("data.json", "json");

// Framework calls
List<Task> tasks = adapter.read(inputStream);

// Framework sets data
context.setData(tasks);
```

### write() - Export Operation

Called by `context.exportData()`:

```java
// Your app
context.exportData("data.json", "json");

// Framework gets data
List<Task> tasks = context.getData();

// Framework calls
adapter.write(tasks, outputStream);
```

### getFormatName() - Registration

Called when adapter is registered:

```java
// Framework asks
String format = adapter.getFormatName();  // Returns "json"

// Registers as
supportedFormats.put("json", adapter);
```

Now `context.exportData("file.json", "json")` knows which adapter to use.

## Format Detection

Check what's supported:

```java
@Override
protected void initialize() {
    // List all supported formats
    List<String> formats = context.getSupportedFormats();
    System.out.println("Available formats: " + String.join(", ", formats));
    // Output: "Available formats: json, csv"

    // Check specific format
    if (context.supportsFormat("json")) {
        System.out.println("JSON is supported");
    }

    // Detect format from filename
    String format = context.detectFormat("data.json");
    System.out.println("Detected: " + format);  // "json"
}
```

## Error Handling

### Unsupported Format

```java
try {
    context.exportData("data.xml", "xml");  // No XML adapter registered!
} catch (IllegalArgumentException e) {
    System.err.println("Format not supported: " + e.getMessage());
    // Export as JSON instead
    context.exportData("data.json", "json");
}
```

### Invalid Data

```java
try {
    context.importData("corrupted.json", "json");
} catch (IOException e) {
    System.err.println("Failed to parse JSON: " + e.getMessage());
    // Use default data
    context.setData(getDefaultData());
}
```

### Type Mismatch

```java
// ❌ Wrong adapter type
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(Task.class)
    .withAdapters(new NoteJSONAdapter())  // Adapter for Note, not Task!
    .run(args);
```

**Error:** `ClassCastException` when importing/exporting

**Fix:** Adapter type must match state type:
```java
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(Task.class)
    .withAdapters(new TaskJSONAdapter())  // ✅ Matches Task.class
    .run(args);
```

## Creating Custom Adapters

For custom formats (XML, YAML, binary), implement `JMiniFormatAdapter`:

```java
public class TaskXMLAdapter implements JMiniFormatAdapter<Task> {
    @Override
    public String getFormatName() {
        return "xml";
    }

    @Override
    public List<Task> read(InputStream input) throws IOException {
        // Your XML deserialization logic
        // ... parse XML from input stream ...
        return tasks;
    }

    @Override
    public void write(List<Task> data, OutputStream output) throws IOException {
        // Your XML serialization logic
        // ... write XML to output stream ...
    }
}
```

See [Creating Custom Adapters](custom-adapter) for detailed guide.

## JMiniFormatAdapter Interface

The base interface for all adapters:

```java
public interface JMiniFormatAdapter<T> {
    /**
     * Deserialize data from input stream
     * @param input Stream to read from
     * @return List of deserialized objects
     */
    List<T> read(InputStream input) throws IOException;

    /**
     * Serialize data to output stream
     * @param data List of objects to serialize
     * @param output Stream to write to
     */
    void write(List<T> data, OutputStream output) throws IOException;

    /**
     * Format name (e.g., "json", "csv", "xml")
     * Used for format detection and registration
     */
    String getFormatName();

    /**
     * Validate input stream (optional)
     * @param input Stream to validate
     * @return true if valid
     */
    default boolean validate(InputStream input) {
        return true;
    }
}
```

## Common Patterns

### Export on Every Change

```java
@Override
protected void run() {
    while (running) {
        // ... user makes changes ...
        tasks.add(new Task());

        // Auto-save after each change
        context.setData(tasks);
        try {
            context.exportData("tasks.json", "json");
        } catch (IOException e) {
            System.err.println("Auto-save failed");
        }
    }
}
```

### Export Multiple Formats at Once

```java
@Override
protected void shutdown() {
    context.setData(data);

    String[] formats = {"json", "csv"};
    for (String format : formats) {
        try {
            context.exportData("data." + format, format);
            System.out.println("Saved as " + format);
        } catch (IOException e) {
            System.err.println(format + " export failed");
        }
    }
}
```

### Format-Agnostic Import

```java
public void importFile(String filePath) {
    String format = context.detectFormat(filePath);

    if (context.supportsFormat(format)) {
        try {
            context.importData(filePath, format);
            System.out.println("Imported from " + format);
        } catch (IOException e) {
            System.err.println("Import failed: " + e.getMessage());
        }
    } else {
        System.err.println("Unsupported format: " + format);
    }
}
```

## Next Steps

- [Creating Custom Adapters](custom-adapter) - Build your own format adapters
- [Import Strategies](import-strategies) - Control how data merges
- [Context API](context) - Full import/export API
- [JMiniAppRunner](runner) - Registering adapters
