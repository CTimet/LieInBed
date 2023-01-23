package io.github.ctimet.lieinbedapp.gui;

import com.jfoenix.controls.JFXScrollPane;
import io.github.ctimet.lieinbedapp.App;
import io.github.ctimet.lieinbedapp.gui.handler.ServerListClickHandler;
import io.github.ctimet.lieinbedapp.gui.ui.ButtonsBox;
import io.github.ctimet.lieinbedapp.gui.ui.WarnList;
import io.github.ctimet.lieinbedapp.gui.util.CSSStyle;
import io.github.ctimet.lieinbedapp.gui.util.SVGUtil;
import io.github.ctimet.lieinbedapp.gui.util.Task;
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
import javafx.scene.layout.VBox;
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
    private static final Logger logger = LoggerFactory.getLogger(io.github.ctimet.lieinbedapp0.gui.Draw.class);

    private static final WarnList warnList = new WarnList(4, 830, 5);
    public static final AnchorPane pane = new AnchorPane();
    public static final JFXScrollPane scrollpane = new JFXScrollPane();
    public static final VBox content = new VBox();
    public static final ButtonsBox box = new ButtonsBox(0, 0, 162, 680);

    public static void draw(Stage stage) {
        box.setChildrenHeight(48)
                .setEachSpace(5)
                .setButtonStyle("/node.css", "button")
                .setInsets(5, 0, 5, 0);
        drawButtonsBox(box);
        CSSStyle.addDefaultStyle(box, "box");

        scrollpane.setPrefWidth(873);
        scrollpane.setPrefHeight(680);
        scrollpane.setLayoutX(162);
        scrollpane.setLayoutY(0);
        scrollpane.setPadding(new Insets(20, 20, 20, 20));

        content.setPrefWidth(873);
        content.setLayoutX(162);
        content.setLayoutY(0);
        content.setSpacing(10);//每个组件之间相隔10

        scrollpane.getChildren().add(content);

        pane.getChildren().addAll(
                new ImageView(new Image(Objects.requireNonNull(Draw.class.getResourceAsStream("/background.png")))),
                box,
                scrollpane,
                warnList
        );

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

        box.addTopButton("服务器列表   ", SVGUtil.list(WHITE, 0.8, 0.8), new ServerListClickHandler())
                .addTopButton("机器人列表   ", SVGUtil.list(WHITE, 0.8, 0.8), event -> {

                })
                .init();
    }

    public static void drawWarn(String warn) {
        warnList.showWarn("  " + warn);
        logger.warn(warn);
    }

    public static void drawErr(Throwable t) {
        drawErrAlert(t, logger);
    }

    public static void drawErrAlert(Throwable t, Logger logger) {
        logger.info("Print err message");
        t.printStackTrace();

        logger.info("Drawing the err stage");
        Alert err = new Alert(Alert.AlertType.ERROR,
                "啊哦，LieInBed抛出了异常，我们已经尽力抢救但是无济于事。\n" +
                        "我们记录了整个异常堆栈并已经将其写入您的粘贴板中，您可以点击确定以打开问题反馈页，点击取消以关闭当前页面。",
                ButtonType.CANCEL, ButtonType.OK);
        err.setHeaderText("Exception");

        StringWriter out = new StringWriter();
        t.printStackTrace(new PrintWriter(out));

        Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
        board.setContents(new StringSelection(out.toString()), null);

        TextArea stackTrace = new TextArea(out.toString());
        err.getDialogPane().setExpandableContent(stackTrace);

        Optional<ButtonType> optional = err.showAndWait();
        if (optional.isPresent() && optional.get() == ButtonType.OK) {
            Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI("https://github.com/CTimetlieinbed/issues/new");
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void drawErrThenClose(Throwable t) {
        logger.info("Print StackTrace");
        t.printStackTrace();

        logger.info("Drawing the err stage");
        Alert err = new Alert(Alert.AlertType.ERROR,
                "啊哦，LieInBed抛出了异常，我们已经尽力抢救但是无济于事。\n" +
                        "我们记录了整个异常堆栈并已经将其写入您的粘贴板中，您可以点击确定以打开问题反馈页，点击取消以关闭当前应用程序。",
                ButtonType.CANCEL, ButtonType.OK);
        err.setHeaderText("Exception");

        StringWriter out = new StringWriter();
        t.printStackTrace(new PrintWriter(out));

        Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
        board.setContents(new StringSelection(out.toString()), null);

        TextArea stackTrace = new TextArea(out.toString());
        err.getDialogPane().setExpandableContent(stackTrace);

        Optional<ButtonType> optional = err.showAndWait();
        if (optional.isPresent()) {
            if (optional.get() == ButtonType.OK) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    URI uri = new URI("https://github.com/CTimetlieinbed/issues/new");
                    desktop.browse(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Task.stop();
            System.exit(1);
        }
    }
}
