package com.jminiapp.examples.counter;

/**
 * Simple model representing a counter value.
 *
 * This is used by the CounterApp to store the current counter state.
 */
public class CounterState {
    private int value;

    /**
     * Create a new counter with value 0.
     */
    public CounterState() {
        this.value = 0;
    }

    /**
     * Create a new counter with a specific value.
     *
     * @param value the initial counter value
     */
    public CounterState(int value) {
        this.value = value;
    }

    /**
     * Get the current counter value.
     *
     * @return the current value
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the counter value.
     *
     * @param value the new value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Increment the counter by 1.
     *
     * @return the new value after incrementing
     */
    public int increment() {
        return ++value;
    }

    /**
     * Decrement the counter by 1.
     *
     * @return the new value after decrementing
     */
    public int decrement() {
        return --value;
    }

    /**
     * Reset the counter to 0.
     */
    public void reset() {
        this.value = 0;
    }

    @Override
    public String toString() {
        return "CounterState{value=" + value + "}";
    }
}
