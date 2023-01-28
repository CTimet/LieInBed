package io.github.ctimet.lieinbedapp.gui.animation;

import com.jfoenix.controls.JFXDialogLayout;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtil {
    public static Animation takeFadeAnimation(Node node, double from, double to, int millis) {
        FadeTransition ft = new FadeTransition();
        ft.setNode(node);
        ft.setFromValue(from);
        ft.setToValue(to);
        ft.setDuration(Duration.millis(millis));
        ft.play();
        return ft;
    }

    public static Animation takeXTranslateAnimation(Node node,
                                               double fromX,
                                               double toX,
                                               int millis) {
        return takeTranslateAnimation(node, fromX, node.getLayoutY(), toX, node.getLayoutY(), millis);
    }

    public static Animation takeYTranslateAnimation(Node node,
                                               double fromY,
                                               double toY,
                                               int millis) {
        return takeTranslateAnimation(node, node.getLayoutX(), fromY, node.getLayoutX(), toY, millis);
    }

    public static Animation takeTranslateAnimation(Node node,
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
        return tt;
    }

    public static void takeJFXLayoutIOTranslation(JFXDialogLayout layout, Node in) {
        for (Node node : layout.getBody()) {
            AnimationUtil.takeXTranslateAnimation(node, node.getLayoutX(), -50, 190);
            AnimationUtil.takeFadeAnimation(node, 1, 0, 190);
        }
        layout.setBody(in);
        AnimationUtil.takeXTranslateAnimation(in, 200, in.getLayoutX(), 190);
        AnimationUtil.takeFadeAnimation(in, 0, 1.0, 190);
    }
}
