package io.github.ctimet.lieinbedapp;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.NumberUtil;
import io.github.ctimet.lieinbedapp.gui.Draw;
import io.github.ctimet.lieinbedapp.gui.util.Task;
import io.github.ctimet.lieinbedapp.properties.AppProperties;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        AppProperties.addPropertiesCheck("connect.timeout",
                value -> NumberUtil.isInteger(value) && Integer.parseInt(value) > 0);
        AppProperties.addPropertiesCheck("connect.scanner.encoding",
                value -> CharsetUtil.parse(value, null) != null);
        AppProperties.addPropertiesCheck("connect.printstream.autoflush",
                value -> value.equals("true") || value.equals("false"));
        AppProperties.addPropertiesCheck("connect.printstream.encoding",
                value -> CharsetUtil.parse(value, null) != null);
        AppProperties.addPropertiesCheck("server.console.gui.line.count",
                value -> NumberUtil.isInteger(value) && Integer.parseInt(value) > 0);

        AppProperties.init();
    }

    @Override
    public void stop() {
        Task.stop();
    }

    @Override
    public void start(Stage stage) {
        Draw.draw(stage);
    }
}
