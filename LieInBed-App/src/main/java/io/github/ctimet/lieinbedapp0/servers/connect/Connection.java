package io.github.ctimet.lieinbedapp0.servers.connect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class Connection {
    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    private final InformationStream stream;
    private final LinkedList<Listener> listeners = new LinkedList<>();
    public Connection(InformationStream stream) {
        this.stream = stream;
    }

    public Connection addListener(Listener listener) {
        listeners.add(listener);
        return this;
    }

    public synchronized void send(String json) {
        stream.println(json);
    }

    public void init() {
        String nextLine;
        while (stream.hasNextLine()) {
            nextLine = stream.nextLine();
            for (Listener l : listeners) {
                l.listen(nextLine);
            }
        }
    }

    public interface Listener {
        void listen(String line);
    }
}
