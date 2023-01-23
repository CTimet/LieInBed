package io.github.ctimet.lieinbedapp0.gui.util;

import io.github.ctimet.lieinbedapp0.App;
import javafx.scene.Parent;

import java.util.Objects;

public class CSSStyle {
    public static void addDefaultStyle(Parent parent, String cssClass) {
        parent.getStylesheets().add(Objects.requireNonNull(App.class.getResource("/node.css")).toExternalForm());
        parent.getStyleClass().add(cssClass);
    }

    public static void addStyle(Parent parent, String cssClass, String cssFilePath) {
        parent.getStylesheets().add(Objects.requireNonNull(App.class.getResource(cssFilePath)).toExternalForm());
        parent.getStyleClass().add(cssClass);
    }
}
