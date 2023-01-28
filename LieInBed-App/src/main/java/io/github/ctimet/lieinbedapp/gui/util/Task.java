package io.github.ctimet.lieinbedapp.gui.util;

import io.github.ctimet.lieinbedapp.gui.Draw;
import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task {
    private static final ExecutorService cachedPool = Executors.newCachedThreadPool();
    private static final ExecutorService fixedPool = Executors.newFixedThreadPool(16);

    public static void runCached(Runnable run) {
        cachedPool.submit(run);
    }

    public static void runFixed(Runnable run) {
        fixedPool.submit(run);
    }

    public static void runInUIThread(Runnable run) {
        Platform.runLater(run);
    }

    public static void delayRunInUIThread(Runnable run, int delay) {
        Task.runCached(() -> {
            try {
                Thread.sleep(delay);
                Task.runInUIThread(run);
            } catch (InterruptedException e) {
                Draw.drawErr(e);
            }
        });
    }

    public static void stop() {
        //一般的，应该将程序关闭前要做的操作放在Task.stop中，在App.instance.stop()方法中将执行Task.stop
        cachedPool.shutdownNow();
        fixedPool.shutdownNow();
    }
}
