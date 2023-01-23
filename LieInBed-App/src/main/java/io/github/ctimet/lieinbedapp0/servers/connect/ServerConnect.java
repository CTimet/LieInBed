package io.github.ctimet.lieinbedapp0.servers.connect;

public interface ServerConnect {
    default void connect(String ip, int port, String password) {
        throw new RuntimeException("调用connect需要覆盖此方法");
    }

    void startServer();
    void stopServer();
    void restartServer();
    void send(String json);
    void addListener(Listener listener);
}
