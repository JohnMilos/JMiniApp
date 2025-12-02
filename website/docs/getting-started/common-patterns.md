---
sidebar_position: 3
---

# Common Patterns

Learn common patterns and best practices for building JMiniApp applications.

## Running Without State Persistence

If you don't need file persistence:

```java
JMiniAppRunner
    .forApp(SimpleApp.class)
    .run(args);
```

### Custom Resource Path

Configure where files are saved:

```java
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(MyState.class)
    .withResourcesPath("data/")
    .run(args);
```

### Multiple Adapters

Support multiple file formats:

```java
JMiniAppRunner
    .forApp(MyApp.class)
    .withState(MyState.class)
    .withAdapters(
        new MyJSONAdapter(),
        new MyCSVAdapter()
    )
    .run(args);
```

### Stateless Applications

Applications that don't need persistent state:

```java
public class StatelessApp extends JMiniApp {
    protected void initialize() {
        // Minimal setup
    }
    
    protected void run() {
        // Do work without state
    }
    
    protected void shutdown() {
        // No state to save
    }
}
```

### Stateful Applications

Applications with persistent data:

```java
public class StatefulApp extends JMiniApp {
    private List<Item> items;
    
    protected void initialize() {
        items = context.getData();
        if (items.isEmpty()) {
            items = new ArrayList<>();
        }
    }
    
    protected void run() {
        // Modify items
        items.add(new Item());
    }
    
    protected void shutdown() {
        context.setData(items);
        context.exportData("json");
    }
}
```

### Multi-Format Applications

Applications supporting multiple export formats:

```java
public static void main(String[] args) {
    JMiniAppRunner
        .forApp(MultiFormatApp.class)
        .withState(Data.class)
        .withAdapters(
            new DataJSONAdapter(),
            new DataCSVAdapter(),
            new DataXMLAdapter()
        )
        .run(args);
}
```