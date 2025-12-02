---
sidebar_position: 4
---

# Templates

Learn JMiniApp through practical, real-world examples. Each example demonstrates different framework features and best practices.

### Basic Template

```java
public class MyApp extends JMiniApp {
    public MyApp(JMiniAppConfig config) {
        super(config);
    }
    
    @Override
    protected void initialize() {
        // Setup
    }
    
    @Override
    protected void run() {
        // Main logic
    }
    
    @Override
    protected void shutdown() {
        // Cleanup
    }
    
    public static void main(String[] args) {
        JMiniAppRunner.forApp(MyApp.class).run(args);
    }
}
```

### With State and Adapters

```java
public class MyApp extends JMiniApp {
    private List<MyData> data;
    
    public MyApp(JMiniAppConfig config) {
        super(config);
    }
    
    @Override
    protected void initialize() {
        data = new ArrayList<>(context.getData());
    }
    
    @Override
    protected void run() {
        // Work with data
    }
    
    @Override
    protected void shutdown() {
        context.setData(data);
        try {
            context.exportData("json");
        } catch (IOException e) {
            System.err.println("Save failed");
        }
    }
    
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(MyApp.class)
            .withState(MyData.class)
            .withAdapters(new MyJSONAdapter())
            .run(args);
    }
}
```

## Contributing Examples

Have a great example? Contribute it!

1. Fork the repository
2. Create your example in `examples/your-app/`
3. Add documentation
4. Submit a pull request

## Next Steps

- [Lifecycle Guide](../guides/lifecycle): Master the application lifecycle
- [Build Your First App](../getting-started/build-your-first-app): Create your first JMiniApp
- [Getting Started](../getting-started/installation): Set up JMiniApp in your project
