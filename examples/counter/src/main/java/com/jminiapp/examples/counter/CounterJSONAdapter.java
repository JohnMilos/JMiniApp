package com.jminiapp.examples.counter;

import com.jminiapp.core.adapters.JSONAdapter;

/**
 * JSON adapter for CounterState objects.
 *
 * <p>This adapter enables the Counter app to import and export counter state
 * to/from JSON files. It leverages the framework's JSONAdapter interface which
 * provides default implementations for serialization using Gson.</p>
 *
 * <p>Example JSON format:</p>
 * <pre>
 * [
 *   {
 *     "value": 42
 *   }
 * ]
 * </pre>
 */
public class CounterJSONAdapter implements JSONAdapter<CounterState> {

    @Override
    public Class<CounterState> getstateClass() {
        return CounterState.class;
    }
}
