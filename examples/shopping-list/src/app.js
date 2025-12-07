/**
 * Simple Shopping List App
 * JMiniApp Example
 * Author: John Milos
 * Description: Basic shopping list with add, remove, and view functionality
 */

class ShoppingListApp {
    constructor() {
        this.items = JSON.parse(localStorage.getItem('simpleShoppingList')) || [];
        this.initializeElements();
        this.setupEventListeners();
        this.displayList();
    }

    initializeElements() {
        this.itemInput = document.getElementById('item-name');
        this.addButton = document.getElementById('add-button');
        this.clearButton = document.getElementById('clear-button');
        this.listElement = document.getElementById('shopping-list');
    }

    setupEventListeners() {
        this.addButton.addEventListener('click', () => this.addItem());
        this.clearButton.addEventListener('click', () => this.clearList());
        this.itemInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.addItem();
        });
    }

    addItem() {
        const itemName = this.itemInput.value.trim();
        
        if (!itemName) {
            alert('Please enter a product name');
            return;
        }
        
        this.items.push({
            id: Date.now(),
            name: itemName
        });
        
        this.saveToLocalStorage();
        this.displayList();
        this.itemInput.value = '';
        this.itemInput.focus();
    }

    removeItem(itemId) {
        this.items = this.items.filter(item => item.id !== itemId);
        this.saveToLocalStorage();
        this.displayList();
    }

    clearList() {
        if (this.items.length === 0) return;
        
        if (confirm('Clear all items from your shopping list?')) {
            this.items = [];
            this.saveToLocalStorage();
            this.displayList();
        }
    }

    saveToLocalStorage() {
        localStorage.setItem('simpleShoppingList', JSON.stringify(this.items));
    }

    displayList() {
        this.listElement.innerHTML = '';
        
        if (this.items.length === 0) {
            this.listElement.innerHTML = '<p class="empty">Your shopping list is empty</p>';
            return;
        }
        
        this.items.forEach(item => {
            const itemElement = document.createElement('div');
            itemElement.className = 'list-item';
            itemElement.innerHTML = `
                <span>${item.name}</span>
                <button onclick="shoppingApp.removeItem(${item.id})" class="delete-btn">Delete</button>
            `;
            this.listElement.appendChild(itemElement);
        });
    }
}

// Start the app
document.addEventListener('DOMContentLoaded', () => {
    window.shoppingApp = new ShoppingListApp();
});
