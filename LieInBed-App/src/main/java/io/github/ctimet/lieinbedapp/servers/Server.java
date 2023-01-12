package io.github.ctimet.lieinbedapp.servers;

import io.github.ctimet.lieinbedapp.servers.connect.ServerConnect;

import java.util.LinkedList;

public class Server {
    private static final LinkedList<Server> SERVER_LIST = new LinkedList<>();
    static {
        SERVER_LIST.add(new Server(null, "test1"));
        SERVER_LIST.add(new Server(null, "test2"));
        SERVER_LIST.add(new Server(null, "test3"));
        SERVER_LIST.add(new Server(null, "testn"));
    }

    private final ServerConnect connect;
    private final String name;

    public Server(ServerConnect connect, String name) {
        this.connect = connect;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static LinkedList<Server> getServerList() {
        return SERVER_LIST;
    }
}
