package com.divy.sanlam.lifecycle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorHolder {
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public static ExecutorService getExecutor() {
        return executor;
    }
}
