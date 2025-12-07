# Shopping List App Tutorial

In this tutorial, we'll build a complete Shopping List application using vanilla JavaScript. This practical example demonstrates how to create a functional web application with add, remove, and view capabilities.

## Prerequisites

- Basic HTML, CSS, and JavaScript knowledge
- Text editor (VS Code recommended)
- Modern web browser

## Step 1: Project Setup

Create the following folder structure:

shopping-list/
- index.html
─ src/
─ app.js
─ style.css

## Step 2: HTML Structure

Create `index.html` with the basic structure:

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping List</title>
    <link rel="stylesheet" href="src/style.css">
</head>
<body>
    <div class="app-container">
        <header>
            <h1>🛒 Shopping List</h1>
        </header>
        
        <main>
            <!-- Form for adding items -->
            <div class="form-container">
                <input type="text" id="item-name" placeholder="Item name">
                <button id="add-button">Add Item</button>
            </div>
            
            <!-- List display -->
            <div id="shopping-list"></div>
        </main>
    </div>
    
    <script src="src/app.js"></script>
</body>
</html>

## Step 3: Basic Styling

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: Arial, sans-serif;
    padding: 20px;
}

.app-container {
    max-width: 600px;
    margin: 0 auto;
}

.form-container {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
}

input {
    flex: 1;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

button {
    padding: 10px 20px;
    background: #4CAF50;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

## Step 4: Javascript

crearte src/aap.js with the basic: 

class ShoppingListApp {
    constructor() {
        this.items = [];
        this.initializeElements();
        this.setupEventListeners();
    }
    
    initializeElements() {
        this.itemNameInput = document.getElementById('item-name');
        this.addButton = document.getElementById('add-button');
        this.shoppingListElement = document.getElementById('shopping-list');
    }
    
    setupEventListeners() {
        this.addButton.addEventListener('click', () => this.addItem());
    }
    
    addItem() {
        const name = this.itemNameInput.value.trim();
        
        if (name) {
            this.items.push({
                id: Date.now(),
                name: name,
                purchased: false
            });
            
            this.render();
            this.itemNameInput.value = '';
        }
    }
    
    render() {
        this.shoppingListElement.innerHTML = this.items
            .map(item => `<div>${item.name}</div>`)
            .join('');
    }
}

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    window.app = new ShoppingListApp();
});

## Step 5: Adding more features

For example: Price an Quantity


