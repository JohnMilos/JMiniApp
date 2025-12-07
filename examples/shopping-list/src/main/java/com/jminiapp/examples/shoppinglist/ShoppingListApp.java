package com.jminiapp.examples.shoppinglist;

import com.jminiapp.core.api.JMiniApp;
import com.jminiapp.core.api.JMiniAppConfig;
import java.util.List;
import java.util.Scanner;

public class ShoppingListApp extends JMiniApp {
    private Scanner scanner;
    private ShoppingListState shoppingList;
    private boolean running;

    public ShoppingListApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        System.out.println("=== Shopping List ===");
        scanner = new Scanner(System.in);
        running = true;
        
        List<ShoppingListState> data = context.getData();
        if (data != null && !data.isEmpty()) {
            shoppingList = data.get(0);
        } else {
            shoppingList = new ShoppingListState();
        }
    }

    @Override
    protected void run() {
        while (running) {
            System.out.println("\n1. Add item");
            System.out.println("2. Remove item");
            System.out.println("3. View items");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            
            String choice = scanner.nextLine();
            
            if (choice.equals("1")) {
                System.out.print("Item name: ");
                shoppingList.addItem(scanner.nextLine());
            } else if (choice.equals("2")) {
                System.out.print("Item number: ");
                try {
                    int index = Integer.parseInt(scanner.nextLine()) - 1;
                    shoppingList.removeItem(index);
                } catch (Exception e) {
                    System.out.println("Invalid number");
                }
            } else if (choice.equals("3")) {
                List<ShoppingListState.ShoppingItem> items = shoppingList.getItems();
                for (int i = 0; i < items.size(); i++) {
                    System.out.println((i + 1) + ". " + items.get(i).getName());
                }
            } else if (choice.equals("4")) {
                running = false;
            }
        }
    }

    @Override
    protected void shutdown() {
        List<ShoppingListState> data = List.of(shoppingList);
        context.setData(data);
        scanner.close();
        System.out.println("Goodbye!");
    }
}

