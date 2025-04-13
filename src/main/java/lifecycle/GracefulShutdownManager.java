package com.divy.sanlam.lifecycle;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class GracefulShutdownManager implements SmartLifecycle {

    private boolean running = false;

    @Override
    public void start() {
        running = true;
    }

    @Override
    public void stop() {
        System.out.println("🛑 Gracefully shutting down...");
        try {
            ExecutorService executor = ExecutorHolder.getExecutor();
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("⚠️ Forcing shutdown of async tasks...");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE; // Shut down last
    }
}
