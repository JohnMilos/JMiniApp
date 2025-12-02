package com.jminiapp.examples.counter;

import com.jminiapp.core.api.JMiniApp;
import com.jminiapp.core.api.JMiniAppConfig;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * A simple counter application demonstrating the JMiniApp framework.
 *
 * This app allows users to:
 * - Increment the counter
 * - Decrement the counter
 * - Reset the counter to 0
 * - View the current counter value
 */
public class CounterApp extends JMiniApp {
    private Scanner scanner;
    private CounterState counter;
    private boolean running;

    public CounterApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        System.out.println("\n=== Counter App ===");
        System.out.println("Welcome to the Counter App!");

        scanner = new Scanner(System.in);
        running = true;

        // Try to load existing counter state from context
        List<CounterState> data = context.getData();
        if (data != null && !data.isEmpty()) {
            counter = data.get(0);
            System.out.println("Loaded existing counter: " + counter.getValue());
        } else {
            counter = new CounterState();
            System.out.println("Starting with new counter at: " + counter.getValue());
        }
    }

    @Override
    protected void run() {
        while (running) {
            displayMenu();
            handleUserInput();
        }
    }

    @Override
    protected void shutdown() {
        // Save the counter state to context
        List<CounterState> data = List.of(counter);
        context.setData(data);

        scanner.close();
        System.out.println("\nFinal counter value: " + counter.getValue());
        System.out.println("Goodbye!");
    }

    private void displayMenu() {
        System.out.println("\n--- Current Value: " + counter.getValue() + " ---");
        System.out.println("1. Increment (+1)");
        System.out.println("2. Decrement (-1)");
        System.out.println("3. Reset to 0");
        System.out.println("4. Export to JSON file");
        System.out.println("5. Import from JSON file");
        System.out.println("6. Exit");
        System.out.print("\nChoose an option: ");
    }

    private void handleUserInput() {
        try {
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    int newValue = counter.increment();
                    System.out.println("Counter incremented to: " + newValue);
                    break;

                case "2":
                    newValue = counter.decrement();
                    System.out.println("Counter decremented to: " + newValue);
                    break;

                case "3":
                    counter.reset();
                    System.out.println("Counter reset to: " + counter.getValue());
                    break;

                case "4":
                    exportToFile();
                    break;

                case "5":
                    importFromFile();
                    break;

                case "6":
                    running = false;
                    System.out.println("\nExiting...");
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1-6.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void exportToFile() {
        try {
            // Save current counter to context before exporting
            context.setData(List.of(counter));

            // Use default filename convention: {appName}.{format}
            context.exportData("json");
            System.out.println("Counter state exported successfully to: Counter.json");
        } catch (IOException e) {
            System.out.println("Error exporting file: " + e.getMessage());
        }
    }

    private void importFromFile() {
        try {
            // Use default filename convention: {appName}.{format}
            context.importData("json");

            // Update local counter from context after import
            List<CounterState> data = context.getData();
            if (data != null && !data.isEmpty()) {
                counter = data.get(0);
                System.out.println("Counter state imported successfully from Counter.json!");
                System.out.println("New value: " + counter.getValue());
            } else {
                System.out.println("Error: No data found in file.");
            }
        } catch (IOException e) {
            System.out.println("Error importing file: " + e.getMessage());
        }
    }
}
