package io.github.ctimet.lieinbedapp.server;

import cn.hutool.json.JSONObject;

public interface ServerConnect {
    void send(JSONObject object, Listener listener);
    void send(String line, Listener listener);
    void startServer();
    void stopServer();
    void stopConnect();
    void restartServer();
    void init();
}
