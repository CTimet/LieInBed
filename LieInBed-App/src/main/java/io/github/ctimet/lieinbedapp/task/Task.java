package io.github.ctimet.lieinbedapp.task;

import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task {
    private static final ExecutorService cachedPool = Executors.newCachedThreadPool();
    private static final ExecutorService fixedPool = Executors.newFixedThreadPool(16);

    public static void runInCachedPool(Runnable run) {
        cachedPool.submit(run);
    }

    public static void runInFixedPool(Runnable run) {
        fixedPool.submit(run);
    }

    public static void runInUIThread(Runnable run) {
        Platform.runLater(run);
    }

    public static void stop() {
        cachedPool.shutdown();
        fixedPool.shutdown();
    }
}
