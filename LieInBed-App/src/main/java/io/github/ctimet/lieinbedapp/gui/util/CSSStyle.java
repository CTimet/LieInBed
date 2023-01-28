package io.github.ctimet.lieinbedapp.gui.util;

import io.github.ctimet.lieinbedapp.App;
import javafx.scene.Parent;

import java.util.Objects;

public class CSSStyle {
    public static void addDefaultStyle(Parent parent, String cssClass) {
        parent.getStyleClass().add(cssClass);
    }

    public static void addStyle(Parent parent, String cssClass, String cssFilePath) {
        parent.getStylesheets().add(Objects.requireNonNull(App.class.getResource(cssFilePath)).toExternalForm());
        parent.getStyleClass().add(cssClass);
    }
}
