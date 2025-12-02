---
sidebar_position: 1
---

# Counter Example Application

A simple counter application demonstrating basic lifecycle and state management.

**Features:**
- Increment/decrement operations
- State persistence
- JSON import/export
- Interactive menu system

**Source Code:** [examples/counter](https://github.com/jminiapp/jminiapp/tree/main/examples/counter)

### Key Concepts Demonstrated

- Application lifecycle (initialize, run, shutdown)
- In-memory state management
- JSON format adapter implementation
- Framework-based import/export

### Quick Start

```bash
cd examples/counter
mvn clean install
mvn exec:java
```

### Code Highlights

**State Model:**
```java
public class CounterState {
    private int value;
    
    public void increment() { value++; }
    public void decrement() { value--; }
    public void reset() { value = 0; }
    public int getValue() { return value; }
}
```

**Application:**
```java
public class CounterApp extends JMiniApp {
    private CounterState counter;

    public CounterApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        try {
            List<CounterState> data = context.getData();
            counter = data.isEmpty() ? new CounterState() : data.get(0);
            System.out.println("Starting with counter at: " + counter.getValue());
        } catch (Exception e) {
            counter = new CounterState();
            System.out.println("Starting with new counter at: 0");
        }
    }

    @Override
    protected void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Current Value: " + counter.getValue() + " ---");
            System.out.println("1. Increment (+1)");
            System.out.println("2. Decrement (-1)");
            System.out.println("3. Exit");
            System.out.print("\nChoose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    counter.increment();
                    break;
                case 2:
                    counter.decrement();
                    break;
                case 3:
                    running = false;
                    break;
            }
        }
    }

    @Override
    protected void shutdown() {
        context.setData(List.of(counter));
        System.out.println("Counter saved. Goodbye!");
    }

    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(CounterApp.class)
            .withState(CounterState.class)
            .withAdapters(new CounterJSONAdapter())
            .run(args);
    }
}
```

**Bootstrap:**
```java
public static void main(String[] args) {
    JMiniAppRunner
        .forApp(CounterApp.class)
        .withState(CounterState.class)
        .withAdapters(new CounterJSONAdapter())
        .run(args);
}
```


This simple example demonstrates the core concepts of JMiniApp: lifecycle management, state handling, and user interaction. The framework takes care of the infrastructure while you focus on building features.