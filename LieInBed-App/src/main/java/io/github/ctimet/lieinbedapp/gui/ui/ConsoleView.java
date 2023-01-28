package io.github.ctimet.lieinbedapp.gui.ui;

import com.jfoenix.controls.JFXTextArea;

import java.util.LinkedList;

public class ConsoleView extends JFXTextArea {
    private final LinkedList<String> content = new LinkedList<>();
    private int maxLineCount = Integer.MAX_VALUE;
    private int lineCount = 0;

    public void setMaxLineCount(int maxLineCount) {
        this.maxLineCount = maxLineCount;
        super.setEditable(false);//禁止用户手动编辑，防止越界
    }

    @Override
    public void appendText(String text) {
        content.add(text);
        if ((lineCount ++) == maxLineCount) {
            super.replaceText(0, content.removeFirst().length(), "");
            lineCount --;
        }

        super.appendText(text);
    }
}
