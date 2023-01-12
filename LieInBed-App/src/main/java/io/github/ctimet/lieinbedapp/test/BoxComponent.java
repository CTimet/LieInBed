package io.github.ctimet.lieinbedapp.test;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class BoxComponent extends JFXButton {
    public double nowLayoutY;
    public EventHandler<ActionEvent> handler;
    public BoxComponent(String label, EventHandler<ActionEvent> handler) {
        this.setText(label);
        this.setOnAction(handler);
    }

    public void setTopNodeAction(EventHandler<ActionEvent> handler) {
        this.handler = handler;
    }
}
