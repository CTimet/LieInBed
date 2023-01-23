package io.github.ctimet.lieinbedapp.gui.util;

import javafx.beans.binding.ObjectBinding;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

public class SVGUtil {
    public static Node console(ObjectBinding<? extends Paint> fill, double width, double height) {
        return createSVGPath("M 6.0928 4.5696 a 0.448 0.448 90 0 0 -0.6272 0.0896 l -1.344 1.792 a 0.4476 0.4476 90 0 0 0 0.5376 l 1.344 1.792 a 0.448 0.448 90 0 0 0.7168 -0.5376 L 5.04 6.72 l 1.1424 -1.5232 a 0.448 0.448 90 0 0 -0.0896 -0.6272 z M 10.4832 8.8704 a 0.448 0.448 90 0 0 0.6272 -0.0896 l 1.344 -1.792 a 0.4476 0.4476 90 0 0 0 -0.5376 l -1.344 -1.792 a 0.448 0.448 90 0 0 -0.7168 0.5376 l 1.1424 1.5232 l -1.1424 1.5232 a 0.448 0.448 90 0 0 0.0896 0.6272 z M 8.2988 4.3828 l -0.896 4.032 a 0.448 0.448 90 1 0 0.8745 0.1944 l 0.896 -4.032 a 0.448 0.448 90 1 0 -0.8745 -0.1944 z M 12.096 8.9094 a 0.448 0.448 90 0 0 -0.448 0.448 V 10.304 h -1.344 a 1.344 1.344 90 0 0 -1.344 1.344 v 1.344 H 3.1786 C 2.908 12.992 2.688 12.7599 2.688 12.475 V 1.861 C 2.688 1.5761 2.908 1.344 3.1786 1.344 h 7.9784 C 11.428 1.344 11.648 1.5761 11.648 1.861 v 1.7423 a 0.448 0.448 90 1 0 0.896 0 V 1.861 C 12.544 1.0819 11.9217 0.448 11.157 0.448 H 3.1786 C 2.4143 0.448 1.792 1.0819 1.792 1.861 v 10.614 C 1.792 13.2541 2.4143 13.888 3.1786 13.888 h 6.0901 c 0.022 0 0.0408 -0.0094 0.0618 -0.0125 c 0.1254 0.0224 0.2585 -0.0036 0.3638 -0.0914 l 2.688 -2.24 a 0.448 0.448 90 0 0 0.142 -0.4744 c 0.0076 -0.0318 0.0197 -0.0618 0.0197 -0.0959 v -1.6164 a 0.448 0.448 90 0 0 -0.448 -0.448 z M 10.304 11.2 h 1.0922 L 9.856 12.4835 V 11.648 a 0.448 0.448 90 0 1 0.448 -0.448 z",
                fill, width, height);
    }

    public static Node back(ObjectBinding<? extends Paint> fill, double width, double height) {
        return createSVGPath("M20,11V13H8L13.5,18.5L12.08,19.92L4.16,12L12.08,4.08L13.5,5.5L8,11H20Z", fill, width,
                height);
    }

    public static Node list(ObjectBinding<? extends Paint> fill, double width, double height) {
        return createSVGPath(
                "M7,5H21V7H7V5M7,13V11H21V13H7M4,4.5A1.5,1.5 0 0,1 5.5,6A1.5,1.5 0 0,1 4,7.5A1.5,1.5 0 0,1 2.5,6A1.5,1.5 0 0,1 4,4.5M4,10.5A1.5,1.5 0 0,1 5.5,12A1.5,1.5 0 0,1 4,13.5A1.5,1.5 0 0,1 2.5,12A1.5,1.5 0 0,1 4,10.5M7,19V17H21V19H7M4,16.5A1.5,1.5 0 0,1 5.5,18A1.5,1.5 0 0,1 4,19.5A1.5,1.5 0 0,1 2.5,18A1.5,1.5 0 0,1 4,16.5Z",
                fill, width, height
        );
    }

    public static Node warning(ObjectBinding<? extends Paint> fill, double width, double height) {
        return createSVGPath(
                "M11,9H13V7H11M12,20C7.59,20 4,16.41 4,12C4,7.59 7.59,4 12,4C16.41,4 20,7.59 20,12C20,16.41 16.41,20 12,20M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M11,17H13V11H11V17Z",
                fill, width, height);
    }

    private static Node createSVGPath(String d, ObjectBinding<? extends Paint> fill, double width, double height) {
        SVGPath path = new SVGPath();
        path.setContent(d);
        path.setScaleX(width);
        path.setScaleY(height);
        if (fill != null)
            path.fillProperty().bind(fill);
        return path;
    }
}
