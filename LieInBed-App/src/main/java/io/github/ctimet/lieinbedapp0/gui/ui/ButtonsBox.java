package io.github.ctimet.lieinbedapp0.gui.ui;

import com.jfoenix.controls.JFXButton;
import io.github.ctimet.lieinbedapp0.gui.animation.AnimationUtil;
import io.github.ctimet.lieinbedapp0.gui.util.CSSStyle;
import io.github.ctimet.lieinbedapp0.task.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ButtonsBox extends VBox {
    private final ObservableList<JFXButton> topButtons = FXCollections.observableArrayList();
    private final ObservableList<JFXButton> downButtons = FXCollections.observableArrayList();

    private String cssFilePath;
    private String cssClass;

    private double childrenHeight;

    public ButtonsBox(double layoutX, double layoutY, double prefWidth, double prefHeight) {
        setLayoutX(layoutX);
        setLayoutY(layoutY);
        setPrefWidth(prefWidth);
        setPrefHeight(prefHeight);
    }

    public ButtonsBox setEachSpace(double eachSpace) {
        setSpacing(eachSpace);
        return this;
    }

    public ButtonsBox setInsets(double top, double right, double bottom, double left) {
        setPadding(new Insets(top, right, bottom, left));
        return this;
    }

    public ButtonsBox setChildrenHeight(double childrenHeight) {
        this.childrenHeight = childrenHeight;
        return this;
    }

    public ButtonsBox setButtonStyle(String cssFilePath, String cssClass) {
        this.cssFilePath = cssFilePath;
        this.cssClass = cssClass;
        return this;
    }

    public ButtonsBox addTopButton(String label, Node graphic, EventHandler<ActionEvent> handler) {
        JFXButton button = new JFXButton(label);

        if (graphic != null)
            button.setGraphic(graphic);
        button.setOnAction(handler);
        button.setCursor(Cursor.HAND);
        button.setPrefHeight(childrenHeight);
        button.setPrefWidth(getPrefWidth() - getPadding().getLeft() - getPadding().getRight());
        CSSStyle.addStyle(button, cssClass, cssFilePath);
        return addTopButton(button);
    }

    public ButtonsBox addTopButton(JFXButton button) {
        topButtons.add(button);
        return this;
    }

    public ButtonsBox clearWithAnimation() {
        AnimationUtil.takeXTranslateAnimation(this, this.getLayoutX(), -1*getPrefWidth()-1, 250);
        getChildren().clear();
        topButtons.clear();
        downButtons.clear();
        Task.delayRunInUIThread(() -> AnimationUtil.takeXTranslateAnimation(this, -1*getPrefWidth()-1, this.getLayoutX(), 250), 250);
        return this;
    }

    public ButtonsBox addDownButton(String label, Node graphic, EventHandler<ActionEvent> handler) {
        JFXButton button = new JFXButton(label);
        button.setGraphic(graphic);
        button.setOnAction(handler);
        button.setCursor(Cursor.HAND);
        button.setPrefHeight(childrenHeight);
        button.setPrefWidth(getPrefWidth() - getPadding().getLeft() - getPadding().getRight());
        CSSStyle.addStyle(button, cssClass, cssFilePath);
        return addDownButton(button);
    }

    public ButtonsBox addDownButton(JFXButton button) {
        downButtons.add(button);
        return this;
    }

    public ButtonsBox init() {
        Pane empty = new Pane();
        double topButtonsHeight = topButtons.size() * (childrenHeight + getSpacing()) + getPadding().getTop();
        double downButtonsHeight = downButtons.size() * (childrenHeight + getSpacing()) + getPadding().getBottom();
        double prefHeight = getPrefHeight() - topButtonsHeight - downButtonsHeight;
        empty.setPrefHeight(prefHeight);
        empty.setPrefWidth(getPrefWidth() - getPadding().getLeft() - getPadding().getRight());

        getChildren().addAll(topButtons);
        getChildren().add(empty);
        getChildren().addAll(downButtons);
        return this;
    }
}
