# Simple Shopping List App

A minimal shopping list application built with vanilla JavaScript. This example demonstrates basic CRUD operations with localStorage persistence.

## Features

- **Add Products**: Simple form to add shopping items
- **Remove Products**: Delete individual items
- **View List**: Display all items in a clean interface
- **Clear All**: Remove all items at once
- **Persistent Storage**: Automatically saves to browser's localStorage
- **Responsive Design**: Works on desktop and mobile

## How to Run

### Quick Start
Simply open `index.html` in your web browser.

### Development
1. Clone this repository
2. Navigate to `examples/shopping-list/`
3. Open `index.html` in your browser

### With Live Server (Recommended for Development)
1. Install VS Code
2. Install the "Live Server" extension
3. Right-click on `index.html` and select "Open with Live Server"

## Usage

1. **Add an item**: Type in the input field and click "Add Product" or press Enter
2. **Remove an item**: Click the "Delete" button next to any item
3. **Clear all items**: Click the "Clear All" button
4. **Data persistence**: Your list is automatically saved and will appear when you reopen the page

## Project Structure
## Code Overview

The application uses:
- **Vanilla JavaScript** (no frameworks)
- **ES6 Classes** for organization
- **localStorage API** for data persistence
- **Modern CSS** with flexbox and responsive design

## Technical Details

- **Storage Key**: `simpleShoppingList`
- **Item Structure**: `{ id: number, name: string }`
- **Browser Support**: Chrome, Firefox, Safari, Edge (modern versions)

## Customization

To modify the application:

1. **Change styles**: Edit `src/style.css`
2. **Modify functionality**: Edit `src/app.js`
3. **Update structure**: Edit `index.html`

Example modifications:
- Add price or quantity fields
- Add item categories
- Implement search functionality
- Add dark mode

## Troubleshooting

**Items not saving?**
- Ensure cookies/localStorage are enabled
- Check browser console for errors (F12 → Console)

**Buttons not working?**
- Refresh the page
- Check JavaScript console for errors

**Layout looks wrong?**
- Clear browser cache
- Check CSS for any errors

## License

This example is part of the JMiniApp framework for educational purposes.

## Author

John Milos
- GitHub: [JohnMilos](https://github.com/JohnMilos)
- Created: December 2024
