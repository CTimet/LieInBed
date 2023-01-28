package io.github.ctimet.remoteserver.connect;

import io.github.ctimet.remoteserver.task.Task;

import java.io.IOException;
import java.net.ServerSocket;

public class Listener {
    private static boolean run = true;
    private static ServerSocket incoming;
    public static void startConnector(int port, String password) throws Exception {
        incoming = new ServerSocket(port);
        while (run) {
            Task.runCached(new ConnectHandler(incoming.accept(), password));
        }
    }

    public static void stop() {
        run = false;
        try {
            incoming.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
