package io.github.ctimet.lieinbedapp0.servers;

import io.github.ctimet.lieinbedapp0.gui.handler.ServerConsoleHandler;
import io.github.ctimet.lieinbedapp0.servers.connect.ServerConnect;

import java.util.LinkedList;

public class Server {
    private static final LinkedList<Server> SERVER_LIST = new LinkedList<>();

    private ServerConnect connect;
    private String name;

    static {

    }

    public Server(ServerConnect connect) {
        this.connect = connect;
    }

    public ServerConnect getConnect() {
        return connect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private ServerConsoleHandler console = new ServerConsoleHandler(this);

    public ServerConsoleHandler getServerConsoleHandler() {
        return console;
    }

    public static LinkedList<Server> getServerList() {
        return SERVER_LIST;
    }
}
