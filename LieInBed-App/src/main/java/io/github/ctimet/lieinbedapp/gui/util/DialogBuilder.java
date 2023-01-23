package io.github.ctimet.lieinbedapp.gui.util;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.LinkedList;

public class DialogBuilder {
    private final LinkedList<Node> actions = new LinkedList<>();
    private final Window window;

    private JFXDialogLayout layout;

    private Label title;
    private Node[] body;

    private JFXAlert<String> alert;
    private JFXButton ok;

    public DialogBuilder(Control control) {
        window = control.getScene().getWindow();
    }

    public DialogBuilder(Pane pane) {
        window = pane.getScene().getWindow();
    }

    public DialogBuilder setTitle(String title) {
        this.title = new Label(title);
        return this;
    }

    public DialogBuilder setBody(Node... nodes) {
        this.body = nodes;
        return this;
    }

    public DialogBuilder addCancelButton(Paint color, EventHandler<ActionEvent> handler) {
        JFXButton cancel = new JFXButton("取消");
        if (color != null)
            cancel.setTextFill(color);
        cancel.setOnAction(event -> {
            alert.hideWithAnimation();
            if (handler != null)
                handler.handle(event);
        });
        cancel.setCancelButton(true);
        cancel.setButtonType(JFXButton.ButtonType.FLAT);
        actions.add(cancel);
        return this;
    }

    public DialogBuilder addOKButton(Paint color, Listener handler) {
        ok = new JFXButton("确定");
        if (color != null)
            ok.setTextFill(color);
        ok.setOnAction(event -> {
            if (handler != null)
                handler.handle(event, alert);
        });
        ok.setDefaultButton(true);
        actions.add(ok);
        return this;
    }

    public JFXButton getOKButton() {
        return ok;
    }

    public DialogBuilder addActionButton(JFXButton button) {
        actions.add(button);
        return this;
    }

    public JFXAlert<String> create() {
        alert = new JFXAlert<>((Stage) window);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        layout = new JFXDialogLayout();
        layout.setHeading(title);
        layout.setBody(body);
        if (!actions.isEmpty())
            layout.setActions(actions);

        alert.setContent(layout);
        return alert;
    }

    public JFXAlert<String> getAlert() {
        return alert;
    }

    public JFXDialogLayout getLayout() {
        return layout;
    }

    public void createAndShow() {
        create().show();
    }

    public interface Listener {
        void handle(ActionEvent event, JFXAlert<String> alert);
    }
}
