package io.github.ctimet.lieinbedapp0;

import io.github.ctimet.lieinbedapp0.gui.Draw;
import io.github.ctimet.lieinbedapp0.properties.AppProperties;
import io.github.ctimet.lieinbedapp0.task.Task;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() {
        AppProperties.checkProperties();
    }

    @Override
    public void start(Stage stage) {
        Draw.draw(stage);
    }

    @Override
    public void stop() {
        Task.stop();
    }
}
