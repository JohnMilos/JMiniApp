---
sidebar_position: 6
---

# JMiniAppConfig

Immutable configuration object for mini applications. Applications are configured using the builder pattern:

```java
JMiniAppRunner
    .forApp(MyApp.class)           // Required: App class
    .withState(MyModel.class)      // Optional: State type
    .named("MyApp")                // Optional: Custom name
    .withResourcesPath("data/")    // Optional: File location
    .withAdapters(...)             // Optional: Format adapters
    .run(args);
```

### Configuration Options

| Option | Description | Default |
|--------|-------------|---------|
| `forApp()` | Application class | Required |
| `withState()` | State model class | None |
| `named()` | Application name | Class simple name |
| `withResourcesPath()` | Base path for files | `src/main/resources/` |
| `withAdapters()` | Format adapters | None |

**Package:** `com.jminiapp.core.api`

**Constants:**
```java
public static final String DEFAULT_RESOURCES_PATH = "src/main/resources/";
```

**Methods:**
```java
String getAppName();
Class<?> getStateClass();
Class<? extends JMiniApp> getAppClass();
List<JMiniFormatAdapter<?>> getAdapters();
String getResourcesPath();
```