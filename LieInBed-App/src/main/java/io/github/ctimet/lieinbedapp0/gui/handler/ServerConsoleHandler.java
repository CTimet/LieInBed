package io.github.ctimet.lieinbedapp0.gui.handler;

import io.github.ctimet.lieinbedapp0.servers.Server;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;

public class ServerConsoleHandler implements EventHandler<ActionEvent> {
    private final TextArea area = new TextArea();

    public ServerConsoleHandler(Server server) {
        server.getConnect().addListener(line -> {

        });
    }

    @Override
    public void handle(ActionEvent event) {
        //动画

    }
}
