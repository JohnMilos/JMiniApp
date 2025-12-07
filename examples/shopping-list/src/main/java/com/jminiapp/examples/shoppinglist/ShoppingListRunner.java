package com.jminiapp.examples.shoppinglist;

import com.jminiapp.core.engine.JMiniAppRunner;

public class ShoppingListRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(ShoppingListApp.class)
            .withState(ShoppingListState.class)
            .named("ShoppingList")
            .run(args);
    }
}

