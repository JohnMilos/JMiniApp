package com.jminiapp.examples.shoppinglist;

import com.jminiapp.core.adapters.JSONAdapter;

public class ShoppingListJSONAdapter implements JSONAdapter<ShoppingListState> {
    @Override
    public Class<ShoppingListState> getStateClass() {
        return ShoppingListState.class;
    }
}


