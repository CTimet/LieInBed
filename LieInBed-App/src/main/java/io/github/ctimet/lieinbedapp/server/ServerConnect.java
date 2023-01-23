package io.github.ctimet.lieinbedapp.server;

public interface ServerConnect {
    void println(String line);
    void addListener(Listener listener, ListenerType type);
    void startServer();
    void stopServer();
    void restartServer();
    void init();
}
