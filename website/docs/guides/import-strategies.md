---
sidebar_position: 9
---

# Import Strategies

Configurable strategies that control how imported data merges with existing application state.

### ImportStrategy

Interface for import merge strategies.

**Package:** `com.jminiapp.core.api`

**Method:**
```java
<T> void merge(List<T> currentData, List<T> importedData);
```

### Built-in Strategies

**Package:** `com.jminiapp.core.api`

**ReplaceStrategy:**
```java
ImportStrategies.REPLACE
```
Replaces all existing data with imported data.

**AppendStrategy:**
```java
ImportStrategies.APPEND
```
Adds imported data to existing data.

**SkipExistingStrategy:**
```java
ImportStrategies.SKIP_EXISTING
```
Only imports items that don't already exist.

**MergeByIdStrategy:**
```java
new MergeByIdStrategy()
```
Merges data based on unique IDs.