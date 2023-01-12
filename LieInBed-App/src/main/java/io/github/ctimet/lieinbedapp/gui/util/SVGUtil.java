package io.github.ctimet.lieinbedapp.gui.util;

import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

public class SVGUtil {
    public static Node list(ObjectBinding<? extends Paint> fill, double width, double height) {
        return createSVGPath(
                "M7,5H21V7H7V5M7,13V11H21V13H7M4,4.5A1.5,1.5 0 0,1 5.5,6A1.5,1.5 0 0,1 4,7.5A1.5,1.5 0 0,1 2.5,6A1.5,1.5 0 0,1 4,4.5M4,10.5A1.5,1.5 0 0,1 5.5,12A1.5,1.5 0 0,1 4,13.5A1.5,1.5 0 0,1 2.5,12A1.5,1.5 0 0,1 4,10.5M7,19V17H21V19H7M4,16.5A1.5,1.5 0 0,1 5.5,18A1.5,1.5 0 0,1 4,19.5A1.5,1.5 0 0,1 2.5,18A1.5,1.5 0 0,1 4,16.5Z",
                fill, width, height
        );
    }

    private static Node createSVGPath(String d, ObjectBinding<? extends Paint> fill, double width, double height) {
        SVGPath path = new SVGPath();
        path.getStyleClass().add("svg");
        path.setContent(d);
        if (fill != null)
            path.fillProperty().bind(fill);

        if (width < 0 || height < 0) {
            StackPane pane = new StackPane(path);
            pane.setAlignment(Pos.CENTER);
            return pane;
        }

        Group svg = new Group(path);
        double scale = Math.min(width / 24, height / 24);
        svg.setScaleX(scale);
        svg.setScaleY(scale);

        return svg;
    }
}