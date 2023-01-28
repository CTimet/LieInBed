package io.github.ctimet.lieinbedapp.server;

import cn.hutool.json.JSONObject;

public interface Listener {
    void listen(JSONObject object);
}
