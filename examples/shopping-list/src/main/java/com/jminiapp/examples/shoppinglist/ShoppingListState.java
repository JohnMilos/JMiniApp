package com.jminiapp.examples.shoppinglist;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListState {
    private List<ShoppingItem> items = new ArrayList<>();
    
    public List<ShoppingItem> getItems() { return items; }
    public void setItems(List<ShoppingItem> items) { this.items = items; }
    
    public void addItem(String name) { items.add(new ShoppingItem(name)); }
    public void removeItem(int index) { if (index >= 0 && index < items.size()) items.remove(index); }
    
    public static class ShoppingItem {
        private String name;
        private boolean purchased;
        
        public ShoppingItem(String name) { this.name = name; this.purchased = false; }
        public String getName() { return name; }
        public boolean isPurchased() { return purchased; }
        public void setPurchased(boolean purchased) { this.purchased = purchased; }
    }
}
