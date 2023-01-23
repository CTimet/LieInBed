package io.github.ctimet.lieinbedapp.server;

import java.util.HashMap;

public class Server {
    private static final HashMap<String, ServerConnect> servers = new HashMap<>();

    public static ServerConnect getServer(String name) {
        return servers.get(name);
    }

    public static void addServer(String name, ServerConnect connect) {
        servers.put(name, connect);
    }

    public static void removeServer(String name) {
        servers.remove(name);
    }

    public static HashMap<String, ServerConnect> getServers() {
        return servers;
    }
}
