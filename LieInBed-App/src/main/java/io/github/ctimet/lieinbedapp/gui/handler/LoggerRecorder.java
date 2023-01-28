package io.github.ctimet.lieinbedapp.gui.handler;

import java.io.StringWriter;

public class LoggerRecorder {
    private final StringWriter debug = new StringWriter();
    private final StringWriter warn = new StringWriter();
    private final StringWriter error = new StringWriter();

    public void recordDebug(String log) {
        warn.append(log);
    }

    public void recordWarn(String log) {
        warn.append(log);
    }

    public void recordError(String log) {
        error.append(log);
    }
}
