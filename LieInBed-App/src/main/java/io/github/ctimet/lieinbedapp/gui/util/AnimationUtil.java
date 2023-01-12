package io.github.ctimet.lieinbedapp.gui.util;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtil {
    public static void takeFadeAnimation(Node node, double from, double to, int millis) {
        FadeTransition ft = new FadeTransition();
        ft.setNode(node);
        ft.setFromValue(from);
        ft.setToValue(to);
        ft.setDuration(Duration.millis(millis));
        ft.play();
    }

    public static void takeXTranslateAnimation(Node node,
                                               double fromX,
                                               double toX,
                                               int millis) {
        takeTranslateAnimation(node, fromX, node.getLayoutY(), toX, node.getLayoutY(), millis);
    }

    public static void takeYTranslateAnimation(Node node,
                                               double fromY,
                                               double toY,
                                               int millis) {
        takeTranslateAnimation(node, node.getLayoutX(), fromY, node.getLayoutX(), toY, millis);
    }

    public static void takeTranslateAnimation(Node node,
                                              double fromX,
                                              double fromY,
                                              double toX,
                                              double toY,
                                              int millis) {
        TranslateTransition tt = new TranslateTransition();
        tt.setNode(node);
        tt.setFromX(fromX);
        tt.setFromY(fromY);
        tt.setToX(toX);
        tt.setToY(toY);
        tt.setDuration(Duration.millis(millis));
        tt.play();
    }
}
