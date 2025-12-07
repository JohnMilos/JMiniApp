# Shopping List App - JMiniApp Java Example

A shopping list manager application demonstrating the JMiniApp framework with CRUD operations and JSON persistence.

## Overview

This example shows how to create a functional shopping list mini-app using JMiniApp core. Users can add, view, mark as purchased, and remove items through an interactive command-line interface.

## Features

- **Add Items**: Add new shopping items with name and quantity
- **List Items**: View all items with their current status
- **Mark as Purchased**: Update item status when purchased
- **Remove Items**: Delete items from the list
- **JSON Persistence**: Automatically save/load data to JSON file
- **Interactive Menu**: User-friendly command-line interface

## Project Structure
shopping-list/
├── pom.xml
├── README.md
└── src/main/java/com/jminiapp/examples/shoppinglist/
├── ShoppingListApp.java # Main application class
├── ShoppingListRunner.java # Bootstrap configuration
├── ShoppingListState.java # Shopping list model
└── ShoppingListJSONAdapter.java # JSON format adapter


## Key Components

### ShoppingListState
A model class that represents the shopping list with methods to:
- `addItem()`: Add new item with name and quantity
- `getItems()`: Retrieve all items
- `markAsPurchased()`: Update item status
- `removeItem()`: Delete item by index
- `clear()`: Remove all items

### ShoppingListJSONAdapter
A format adapter that enables JSON import/export for `ShoppingListState`:
- Implements `JSONAdapter<ShoppingListState>` from the framework
- Registers with the framework during app bootstrap
- Provides automatic serialization/deserialization of shopping items

### ShoppingListApp
The main application class that extends `JMiniApp` and implements:
- `initialize()`: Set up the app and load existing shopping list
- `run()`: Main loop displaying menu and handling user input
- `shutdown()`: Save the shopping list before exiting
- Uses framework's `context.importData()` and `context.exportData()` for file operations

### ShoppingListRunner
Bootstrap configuration that:
- Registers the `ShoppingListJSONAdapter` with `.withAdapters()`
- Configures the app name and model class
- Launches the application

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build the project

From the **project root** (not the examples/shopping-list directory):
```bash
mvn clean install
This will build both the jminiapp-core module and the shopping-list example.

Run the application
Option 1: Using Maven exec plugin (from the examples/shopping-list directory)

cd examples/shopping-list
mvn exec:java


Option 2: Using the packaged JAR (from the examples/shopping-list directory)

cd examples/shopping-list
java -jar target/shopping-list-app.jar

Option 3: From the project root

cd examples/shopping-list && mvn exec:java


Usage Example
Basic Operations


=== Shopping List App ===
Welcome to the Shopping List App!

--- Shopping List Menu ---
1. Add new item
2. View all items
3. Mark item as purchased
4. Remove item
5. Clear all items
6. Export to JSON
7. Import from JSON
8. Exit

Choose an option: 1
Enter item name: Milk
Enter quantity: 2
Item added: Milk (Quantity: 2)

--- Shopping List Menu ---
1. Add new item
2. View all items
3. Mark item as purchased
4. Remove item
5. Clear all items
6. Export to JSON
7. Import from JSON
8. Exit

Choose an option: 2
Current Shopping List:
1. [ ] Milk - Quantity: 2
2. [ ] Bread - Quantity: 1
3. [✓] Eggs - Quantity: 12 (Purchased)

Export to JSON

--- Shopping List Menu ---
1. Add new item
2. View all items
3. Mark item as purchased
4. Remove item
5. Clear all items
6. Export to JSON
7. Import from JSON
8. Exit

Choose an option: 6
Enter filename to export (e.g., shopping-list.json): my-list.json
Shopping list exported successfully to: /path/to/my-list.json
The exported JSON file will look like:

[
  {
    "name": "Milk",
    "quantity": 2,
    "purchased": false
  },
  {
    "name": "Bread",
    "quantity": 1,
    "purchased": false
  }
]

Import from JSON

--- Shopping List Menu ---
1. Add new item
2. View all items
3. Mark item as purchased
4. Remove item
5. Clear all items
6. Export to JSON
7. Import from JSON
8. Exit

Choose an option: 7
Enter filename to import (e.g., shopping-list.json): my-list.json
Shopping list imported successfully! Loaded 2 items.

Next Steps
Try extending this example by:

Adding categories or tags to items

Implementing search/filter functionality

Adding due dates for items

Implementing unit price and total calculation

Adding CSV export functionality using the CSVAdapter

Creating a GUI version

Adding data validation and error handling

Author: Joaquin Roberto Gutierrez Ramirez
GitHub: JohnMilos
Date: December 2024
