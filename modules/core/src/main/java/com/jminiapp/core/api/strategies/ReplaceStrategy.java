package com.jminiapp.core.api.strategies;

import java.util.List;

import com.jminiapp.core.api.ImportStrategy;

public class ReplaceStrategy implements ImportStrategy {
    @Override
    public <T> void merge(List<T> currentData, List<T> importedData) {
        currentData.clear();
        currentData.addAll(importedData);
    }
}