# Shopping List App Tutorial

This tutorial guides you through building a complete shopping list application using the JMiniApp framework. You'll create a functional Java application with CRUD operations and JSON persistence.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Basic understanding of Java and object-oriented programming
- JMiniApp framework installed (included in the project)

## Overview

The Shopping List App allows users to manage their shopping items through a command-line interface. Key features include:
- Adding items with name and quantity
- Viewing the current shopping list
- Marking items as purchased
- Removing items
- Automatic JSON persistence

## Step 1: Project Setup

First, ensure you're in the correct directory structure within the JMiniApp project:
jminiapp/
├── examples/
│ └── shopping-list/
│ ├── pom.xml
│ └── src/main/java/com/jminiapp/examples/shoppinglist/
└── website/
└── docs/examples/
└── shopping-list.md (yhis file)


If the `shopping-list` directory doesn't exist, create it with the proper Maven structure.

## Step 2: Creating the ShoppingListState

The `ShoppingListState` class represents the core data model for our application. It manages the list of shopping items.

Create `ShoppingListState.java`:

```java
package com.jminiapp.examples.shoppinglist;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state of a shopping list application.
 * Contains a list of shopping items with their details.
 */
public class ShoppingListState {
    private List<ShoppingItem> items = new ArrayList<>();
    
    /**
     * Represents a single shopping item.
     */
    public static class ShoppingItem {
        private String name;
        private int quantity;
        private boolean purchased;
        
        public ShoppingItem() {}
        
        public ShoppingItem(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
            this.purchased = false;
        }
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public boolean isPurchased() { return purchased; }
        public void setPurchased(boolean purchased) { this.purchased = purchased; }
        
        @Override
        public String toString() {
            String status = purchased ? "✓" : " ";
            return String.format("[%s] %s - Quantity: %d", status, name, quantity);
        }
    }
    
    /**
     * Adds a new item to the shopping list.
     */
    public void addItem(String name, int quantity) {
        items.add(new ShoppingItem(name, quantity));
    }
    
    /**
     * Returns all items in the shopping list.
     */
    public List<ShoppingItem> getItems() {
        return new ArrayList<>(items); // Return defensive copy
    }
    
    /**
     * Marks an item as purchased by its index.
     */
    public boolean markAsPurchased(int index) {
        if (index >= 0 && index < items.size()) {
            items.get(index).setPurchased(true);
            return true;
        }
        return false;
    }
    
    /**
     * Removes an item by its index.
     */
    public boolean removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            return true;
        }
        return false;
    }
    
    /**
     * Clears all items from the shopping list.
     */
    public void clear() {
        items.clear();
    }
    
    /**
     * Returns the number of items in the shopping list.
     */
    public int getItemCount() {
        return items.size();
    }
}


## Step 2: Creating the ShoppingListJSONAdapter
The ShoppingListJSONAdapter handles JSON serialization and deserialization for our state.
It implements the JSONAdapter interface from the JMiniApp framework.

Create ShoppingListJSONAdapter.java:

package com.jminiapp.examples.shoppinglist;

import com.jminiapp.core.adapters.JSONAdapter;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

/**
 * JSON adapter for ShoppingListState.
 * Enables importing and exporting shopping list data to/from JSON files.
 */
public class ShoppingListJSONAdapter implements JSONAdapter<ShoppingListState> {
    
    /**
     * Returns the type reference for JSON deserialization.
     */
    @Override
    public TypeReference<List<ShoppingListState>> getTypeReference() {
        return new TypeReference<List<ShoppingListState>>() {};
    }
    
    /**
     * Returns the display name for this adapter.
     */
    @Override
    public String getDisplayName() {
        return "Shopping List";
    }
    
    /**
     * Returns the default file extension for JSON files.
     */
    @Override
    public String getDefaultExtension() {
        return "json";
    }
}

## Step 3: Creating the ShoppingListApp
The ShoppingListApp class extends JMiniApp and contains the main application logic, including
the user interface menu.

Create ShoppingListApp.java:

package com.jminiapp.examples.shoppinglist;

import com.jminiapp.core.JMiniApp;
import java.util.Scanner;

/**
 * Main application class for the Shopping List app.
 */
public class ShoppingListApp extends JMiniApp<ShoppingListState> {
    private final Scanner scanner = new Scanner(System.in);
    
    /**
     * Initializes the application and loads existing state.
     */
    @Override
    public void initialize() {
        System.out.println("=== Shopping List App ===");
        System.out.println("Welcome to the Shopping List App!");
        
        // Load existing data if available
        if (getContext().hasData()) {
            System.out.println("Loaded existing shopping list.");
        } else {
            System.out.println("Starting with empty shopping list.");
        }
    }
    
    /**
     * Main application loop with menu.
     */
    @Override
    public void run() {
        boolean running = true;
        
        while (running) {
            printMenu();
            int choice = getMenuChoice();
            running = handleMenuChoice(choice);
        }
    }
    
    /**
     * Prints the main menu.
     */
    private void printMenu() {
        System.out.println("\n--- Shopping List Menu ---");
        System.out.println("1. Add new item");
        System.out.println("2. View all items");
        System.out.println("3. Mark item as purchased");
        System.out.println("4. Remove item");
        System.out.println("5. Clear all items");
        System.out.println("6. Export to JSON");
        System.out.println("7. Import from JSON");
        System.out.println("8. Exit");
    }
    
    /**
     * Gets user's menu choice with validation.
     */
    private int getMenuChoice() {
        System.out.print("\nChoose an option: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Handles the user's menu choice.
     */
    private boolean handleMenuChoice(int choice) {
        switch (choice) {
            case 1 -> addItem();
            case 2 -> viewItems();
            case 3 -> markAsPurchased();
            case 4 -> removeItem();
            case 5 -> clearItems();
            case 6 -> exportToJSON();
            case 7 -> importFromJSON();
            case 8 -> {
                System.out.println("Goodbye!");
                return false;
            }
            default -> System.out.println("Invalid option. Please try again.");
        }
        return true;
    }
    
    /**
     * Adds a new item to the shopping list.
     */
    private void addItem() {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter quantity: ");
        try {
            int quantity = Integer.parseInt(scanner.nextLine());
            getState().addItem(name, quantity);
            System.out.println("Item added: " + name + " (Quantity: " + quantity + ")");
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a number.");
        }
    }
    
    /**
     * Displays all items in the shopping list.
     */
    private void viewItems() {
        var items = getState().getItems();
        if (items.isEmpty()) {
            System.out.println("Your shopping list is empty.");
            return;
        }
        
        System.out.println("Current Shopping List:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }
    }
    
    /**
     * Marks an item as purchased.
     */
    private void markAsPurchased() {
        viewItems();
        if (getState().getItemCount() == 0) return;
        
        System.out.print("Enter item number to mark as purchased: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (getState().markAsPurchased(index)) {
                System.out.println("Item marked as purchased.");
            } else {
                System.out.println("Invalid item number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
    
    /**
     * Removes an item from the shopping list.
     */
    private void removeItem() {
        viewItems();
        if (getState().getItemCount() == 0) return;
        
        System.out.print("Enter item number to remove: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (getState().removeItem(index)) {
                System.out.println("Item removed.");
            } else {
                System.out.println("Invalid item number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
    
    /**
     * Clears all items from the shopping list.
     */
    private void clearItems() {
        System.out.print("Are you sure you want to clear all items? (yes/no): ");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            getState().clear();
            System.out.println("All items cleared.");
        }
    }
    
    /**
     * Exports the shopping list to a JSON file.
     */
    private void exportToJSON() {
        System.out.print("Enter filename to export (e.g., shopping-list.json): ");
        String filename = scanner.nextLine();
        
        try {
            getContext().exportData(filename);
            System.out.println("Shopping list exported successfully to: " + filename);
        } catch (Exception e) {
            System.out.println("Error exporting: " + e.getMessage());
        }
    }
    
    /**
     * Imports a shopping list from a JSON file.
     */
    private void importFromJSON() {
        System.out.print("Enter filename to import (e.g., shopping-list.json): ");
        String filename = scanner.nextLine();
        
        try {
            getContext().importData(filename);
            System.out.println("Shopping list imported successfully!");
            System.out.println("Loaded " + getState().getItemCount() + " items.");
        } catch (Exception e) {
            System.out.println("Error importing: " + e.getMessage());
        }
    }
    
    /**
     * Cleans up resources before exit.
     */
    @Override
    public void shutdown() {
        scanner.close();
        System.out.println("Shopping List App shutting down...");
    }
}


## Step 4: Creating the ShoppingListRunner
The ShoppingListRunner configures and launches the application, registering the JSON adapter.

Create ShoppingListRunner.java:


package com.jminiapp.examples.shoppinglist;

import com.jminiapp.core.JMiniAppBuilder;

/**
 * Runner class that configures and launches the ShoppingListApp.
 */
public class ShoppingListRunner {
    public static void main(String[] args) {
        new JMiniAppBuilder<>(ShoppingListState.class)
            .withAppName("Shopping List")
            .withAdapters(new ShoppingListJSONAdapter())
            .build(ShoppingListApp::new)
            .run();
    }
}


## Step 5: Building and Running
Build the Application
From the project root directory:


mvn clean install


This builds both the JMiniApp core framework and all examples including the shopping list app.

Run the Application
Navigate to the shopping list example directory:

cd examples/shopping-list

Run using Maven

mvn exec:java

Or compile and run directly:

mvn clean compile
mvn exec:java -Dexec.mainClass="com.jminiapp.examples.shoppinglist.ShoppingListRunner"


## Step 6: Testing the Application
After launching, you should see:

=== Shopping List App ===
Welcome to the Shopping List App!
Starting with empty shopping list.

--- Shopping List Menu ---
1. Add new item
2. View all items
3. Mark item as purchased
4. Remove item
5. Clear all items
6. Export to JSON
7. Import from JSON
8. Exit
Try adding items, marking them as purchased, and exporting/importing JSON data.

JSON File Format
When you export your shopping list, it creates a JSON file like this:


[
  {
    "items": [
      {
        "name": "Milk",
        "quantity": 2,
        "purchased": false
      },
      {
        "name": "Bread", 
        "quantity": 1,
        "purchased": true
      }
    ]
  }
]


Extending the Application
Here are some ideas to enhance the shopping list app:

Add item categories: Group items by category (produce, dairy, etc.)

Price tracking: Add price field and calculate total cost

Due dates: Add expiration dates or purchase deadlines

Multiple lists: Support for different shopping lists

Unit types: Support different units (kg, liters, pieces)

Search/filter: Find items by name or category

CSV export: Add CSVAdapter support for spreadsheet compatibility

GUI interface: Create a graphical version using JavaFX or Swing

Troubleshooting
Common Issues
"No suitable adapter found": Ensure ShoppingListJSONAdapter is registered in ShoppingListRunner

JSON file not found: The app creates the file on first export; check file permissions

Invalid JSON format: Ensure exported files aren't manually edited incorrectly

Maven dependencies: Run mvn clean install from the project root first

Debug Tips
Add System.out.println() statements to trace execution flow

Check the JSON file format with a text editor

Verify all Java files are in the correct package structure

Ensure Java version is 17+

Conclusion
You've successfully built a complete shopping list application with the JMiniApp framework! This example demonstrates:

Creating a data model with ShoppingListState

Implementing JSON persistence with ShoppingListJSONAdapter

Building an interactive CLI with ShoppingListApp

Configuring the application with ShoppingListRunner

Using the JMiniApp framework for state management and file operations

This pattern can be adapted to build many other types of mini-applications with persistent state and user interaction.

Tutorial by Joaquin Roberto Gutierrez Ramirez
GitHub: JohnMilos
December 2024



