package com.jminiapp.examples.counter;

import com.jminiapp.core.engine.JMiniAppRunner;

public class CounterAppRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(CounterApp.class)
            .withState(CounterState.class)
            .withAdapters(new CounterJSONAdapter())
            //.withResourcesPath("test-data/")  // Custom import/export path
            .named("Counter")
            .run(args);
    }
}
