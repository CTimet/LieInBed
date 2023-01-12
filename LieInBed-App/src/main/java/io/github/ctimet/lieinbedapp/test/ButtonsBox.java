package io.github.ctimet.lieinbedapp.test;

import com.jfoenix.controls.JFXButton;
import io.github.ctimet.lieinbedapp.App;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Objects;

public class ButtonsBox extends StackPane {
    private double leftSpace;
    private double childrenHeight;
    private double eachSpace;
    private boolean isExpand = false;
    private final ObservableList<BoxComponent> subButtons = FXCollections.observableArrayList();
    public ButtonsBox(double layoutX, double layoutY, double prefWidth, double prefHeight) {
        setLayoutX(layoutX);
        setLayoutY(layoutY);
        setPrefWidth(prefWidth);
        setPrefHeight(prefHeight);
    }

    public ButtonsBox setLeftSpace(double leftSpace) {
        this.leftSpace = leftSpace;
        return this;
    }

    public ButtonsBox setChildrenHeight(double cHeight) {
        this.childrenHeight = cHeight;
        return this;
    }

    public ButtonsBox setEachSpace(double eachSpace) {
        this.eachSpace = eachSpace;
        return this;
    }

    public void addTopNode(BoxComponent button) {
        button.setOnAction(event -> {
            int nums = 0;
            for (BoxComponent subButton : subButtons) {
                if (isExpand) {
                    takeYMoveAnimation(subButton, 190, subButton.nowLayoutY, getLayoutY());
                } else {
                    takeYMoveAnimation(subButton, 190, subButton.nowLayoutY, (nums + 1) * (childrenHeight + eachSpace));
                }
                nums ++;
            }
            isExpand = !isExpand;
            if (button.handler != null) {
                button.handler.handle(event);
            }
        });

        setStyle(button);
    }

    public ButtonsBox addNode(BoxComponent button) {
        subButtons.add(button);
        setStyle(button);
        return this;
    }

    private void setStyle(JFXButton button) {
        button.getStylesheets().add(Objects.requireNonNull(App.class.getResource("/node.css")).toExternalForm());
        button.getStyleClass().add("buttonNotClick");
        button.setTextFill(Color.WHITE);
        button.setLayoutX(getLayoutX() + leftSpace);
        button.setLayoutY(getLayoutY());
        button.setPrefWidth(getPrefWidth() - leftSpace);
        button.setPrefHeight(childrenHeight);
        getChildren().add(button);
    }

    private static void takeYMoveAnimation(BoxComponent node, int millis, double fromY, double toY) {
        TranslateTransition tt = new TranslateTransition();
        tt.setNode(node);
        tt.setDuration(Duration.millis(millis));
        tt.setFromY(fromY);
        tt.setToY(toY);
        tt.play();
        node.nowLayoutY = toY;
    }
}
