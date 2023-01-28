package io.github.ctimet.lieinbedapp.gui.handler;

import com.jfoenix.controls.JFXTextField;
import io.github.ctimet.lieinbedapp.gui.Draw;
import io.github.ctimet.lieinbedapp.gui.animation.AnimationUtil;
import io.github.ctimet.lieinbedapp.gui.ui.ConsoleView;
import io.github.ctimet.lieinbedapp.gui.util.CSSStyle;
import io.github.ctimet.lieinbedapp.properties.AppProperties;
import io.github.ctimet.lieinbedapp.server.ServerConnect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.HashMap;

public class ServerConsoleHandler implements EventHandler<ActionEvent> {
    private final ServerConnect connect;
    private final ConsoleView area = new ConsoleView();
    private final JFXTextField cmd = new JFXTextField();
    private final HashMap<LogLevel, LoggerRecorder> loggerRecorder = new HashMap<>();
    
    public ServerConsoleHandler(ServerConnect connect) {
        this.connect = connect;

        loggerRecorder.put(LogLevel.DEBUG, new LoggerRecorder());
        loggerRecorder.put(LogLevel.WARN, new LoggerRecorder());
        loggerRecorder.put(LogLevel.ERROR, new LoggerRecorder());

//        Draw.content.setSpacing(0);
    }

    @Override
    public void handle(ActionEvent event) {
        cmd.setOnAction(e -> {
            area.appendText("> " + cmd.getText() + "\n");
//            connect.println(cmd.getText());

            cmd.setText("");
        });

        //绘制UI
        area.setMaxLineCount(Integer.parseInt(AppProperties.getProperty("server.console.gui.line.count")));
        area.setPrefHeight(600);
        cmd.setPromptText("在此处键入命令");
        CSSStyle.addDefaultStyle(cmd, "console");
        CSSStyle.addDefaultStyle(area, "console");
        Draw.drawContent(area, cmd);
        AnimationUtil.takeFadeAnimation(area, 0.3, 0.73, 340);
    }


    public void appendText(String text) {

    }

    //我们只负责记录DEBUG，WARN，ERROR 3个日志级别。
    enum LogLevel {
        DEBUG,
        WARN,
        ERROR
    }
}
