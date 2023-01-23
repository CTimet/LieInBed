package io.github.ctimet.lieinbedapp0.gui;

import com.jfoenix.controls.JFXScrollPane;
import io.github.ctimet.lieinbedapp0.gui.handler.ServerListClickHandler;
import io.github.ctimet.lieinbedapp0.gui.ui.ButtonsBox;
import io.github.ctimet.lieinbedapp0.gui.ui.WarnList;
import io.github.ctimet.lieinbedapp0.gui.util.CSSStyle;
import io.github.ctimet.lieinbedapp0.gui.util.SVGUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public class Draw {
    private static final Logger logger = LoggerFactory.getLogger(Draw.class);

    public static final AnchorPane pane = new AnchorPane();
    private static final WarnList warnList = new WarnList(4, 830, 5);

    public static final JFXScrollPane content = new JFXScrollPane();
    public static ButtonsBox box;

    public static void draw(Stage stage) {
        box = new ButtonsBox(0, 0, 162, 680)
                .setChildrenHeight(48)
                .setEachSpace(5)
                .setButtonStyle("/node.css", "button")
                .setInsets(5, 0, 5, 0);
        drawButtonsBox(box);
        CSSStyle.addDefaultStyle(box, "box");

        content.setPrefWidth(873);
        content.setPrefHeight(680);
        content.setLayoutX(162);
        content.setLayoutY(0);
        content.setPadding(new Insets(20, 20, 20, 20));

        pane.getChildren().addAll(
                new ImageView(new Image(Objects.requireNonNull(Draw.class.getResourceAsStream("/background.png")))),
                box,
                content,
                warnList
        );

        pane.setStyle("-fx-background-color: #f5f5f5;");

        Scene scene = new Scene(pane, 1035, 680);
        stage.setScene(scene);
        stage.setTitle("Lie In Bed");

        Image icon = new Image(Objects.requireNonNull(Draw.class.getResourceAsStream("/icon.png")));
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.show();
    }

    public static void drawButtonsBox(ButtonsBox box) {
        ObjectBinding<Color> WHITE = Bindings.createObjectBinding(() -> Color.WHITE);

        box.addTopButton("服务器列表   ", SVGUtil.list(WHITE, 0.8, 0.8), new ServerListClickHandler(pane))
                .addTopButton("机器人列表   ", SVGUtil.list(WHITE, 0.8, 0.8), event -> {

                })
                .init();
    }

    public static void drawWarn(String warn) {
        warnList.showWarn("  " + warn);
        logger.warn(warn);
    }

    public static void drawErr(Throwable t) {
        io.github.ctimet.lieinbedapp.gui.Draw.drawErrAlert(t, logger);
    }
}
