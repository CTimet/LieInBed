package io.github.ctimet.remoteserver.task;

import io.github.ctimet.remoteserver.connect.Listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task {
    private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(16);

    public static void runCached(Runnable run) {
        cachedThreadPool.submit(run);
    }

    public static void runFixed(Runnable run) {
        fixedThreadPool.submit(run);
    }

    public static void stop() {
        cachedThreadPool.shutdown();
        fixedThreadPool.shutdown();
    }

    public static void closeRemoteServer() {
        System.exit(0);
        Listener.stop();
        Task.stop();
    }
}
