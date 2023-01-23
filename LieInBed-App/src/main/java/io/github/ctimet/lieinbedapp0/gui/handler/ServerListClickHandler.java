package io.github.ctimet.lieinbedapp0.gui.handler;

import cn.hutool.core.util.NumberUtil;
import com.jfoenix.controls.*;
import io.github.ctimet.lieinbedapp0.gui.Draw;
import io.github.ctimet.lieinbedapp0.gui.animation.AnimationUtil;
import io.github.ctimet.lieinbedapp0.gui.util.DialogBuilder;
import io.github.ctimet.lieinbedapp0.servers.Server;
import io.github.ctimet.lieinbedapp0.servers.connect.ServerConnect;
import io.github.ctimet.lieinbedapp0.servers.connect.impl.RemoteConnector;
import io.github.ctimet.lieinbedapp0.task.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.LinkedList;

public class ServerListClickHandler implements EventHandler<ActionEvent> {
    private final AnchorPane pane;
    private DialogBuilder builder;
    public ServerListClickHandler(AnchorPane pane) {
        this.pane = pane;
    }

    @Override
    public void handle(ActionEvent event) {
        DialogBuilder chooseServerDialog = new DialogBuilder(pane)
                    .setTitle("选择一个服务器")
                    .addCancelButton(Color.BLACK, null)
                    .addOKButton(Color.BLACK, null);
        builder = chooseServerDialog;

        VBox content = new VBox();

        LinkedList<JFXButton> buttons = new LinkedList<>();
        Server.getServerList().forEach(s -> {
            JFXButton button = new JFXButton(s.getName());
            button.setOnAction(serverButtonAction -> {

            });
            buttons.add(button);
        });
        JFXButton add = new JFXButton("+ 添加新的服务器");
        add.setOnAction(addAction -> {
            JFXDialogLayout layout = chooseServerDialog.getLayout();
            for (Node node : layout.getBody()) {
                AnimationUtil.takeXTranslateAnimation(node, node.getLayoutX(), -50, 190);
                AnimationUtil.takeFadeAnimation(node, 1, 0, 190);
            }

            //在同一时间我们还要准备要入场的新组件
            VBox serverType = new VBox();
            JFXButton localServer = new JFXButton("+ 本地服务器");
            localServer.setOnAction(new AddLocalServer(layout));
            JFXButton remoteServer = new JFXButton("+ 远程服务器");
            remoteServer.setOnAction(new AddRemoteServer(builder));
            serverType.getChildren().addAll(localServer, remoteServer);

            Task.delayRunInUIThread(() -> {
                layout.setHeading(new Label("选择新建服务器类型"));

                layout.setBody(serverType);
                //播放新组件的入场动画
                AnimationUtil.takeXTranslateAnimation(serverType, 200, serverType.getLayoutX(), 190);
                AnimationUtil.takeFadeAnimation(serverType, 0, 1.0, 190);
                //播放动画需要在旧组件移除后再播放，否则会出现问题
            }, 190);
        });
        buttons.add(add);
        content.getChildren().addAll(buttons);

        chooseServerDialog.setBody(content).createAndShow();
    }

    public static class AddRemoteServer implements EventHandler<ActionEvent> {
        private final JFXDialogLayout layout;
        private final DialogBuilder builder;
        public AddRemoteServer(DialogBuilder builder) {
            this.builder = builder;
            this.layout = builder.getLayout();
        }

        @Override
        public void handle(ActionEvent event) {
            for (Node node : layout.getBody()) {
                AnimationUtil.takeXTranslateAnimation(node, node.getLayoutX(), -50, 190);
                AnimationUtil.takeFadeAnimation(node, 1, 0, 190);
            }

            //准备要入场的新组
            VBox vBox = new VBox();
            JFXTextField ip_input = new JFXTextField();
            ip_input.setPromptText("IP");

            JFXTextField port_input = new JFXTextField();
            port_input.setPromptText("端口");

            JFXPasswordField password_input = new JFXPasswordField();
            password_input.setPromptText("连接密码");

            vBox.getChildren().addAll(ip_input, port_input, password_input);
            vBox.setSpacing(9);

            Task.delayRunInUIThread(() -> {
                layout.setHeading(new Label("填写远程服务器参数"));

                layout.setBody(vBox);

                builder.getOKButton().setOnAction(e -> {
                    String ip = null;
                    int port = -1;
                    String password = null;
                    if (ip_input.getText().equals("")) {
                        Draw.drawWarn("需要填写远程服务器IP地址！");
                    } else {
                        ip = ip_input.getText();
                    }
                    if (port_input.getText().equals("")) {
                        Draw.drawWarn("需要填写连接端口！");
                    }
                    if (NumberUtil.isInteger(port_input.getText())) {
                        port = Integer.parseInt(port_input.getText());
                        if (port < 0 || port > 65535) {
                            Draw.drawWarn("端口必须介于 0~65535 之间");
                            port = -1;
                        }
                    } else {
                        Draw.drawWarn("端口必须为一个整数数字！");
                    }
                    if (password_input.getText().equals("")) {
                        Draw.drawWarn("必须提供连接密码！");
                    } else {
                        password = password_input.getText();
                    }
                    if (ip != null && port != -1 && password != null) {
                        new ConnectServer(ip, port, password, builder).handle(e);
                    }
                });

                AnimationUtil.takeXTranslateAnimation(vBox, 200, vBox.getLayoutX(), 190);
                AnimationUtil.takeFadeAnimation(vBox, 0, 1.0, 190);
            }, 190);
        }
    }

    public static class ConnectServer implements EventHandler<ActionEvent> {
        private final JFXDialogLayout layout;
        private final String ip;
        private final int port;
        private final String password;
        private final DialogBuilder builder;
        public ConnectServer(String ip, int port, String password, DialogBuilder builder) {
            this.layout = builder.getLayout();
            this.ip = ip;
            this.port = port;
            this.password = password;
            this.builder = builder;
        }

        @Override
        public void handle(ActionEvent event) {
            for (Node node : layout.getBody()) {
                AnimationUtil.takeXTranslateAnimation(node, node.getLayoutX(), -50, 190);
                AnimationUtil.takeFadeAnimation(node, 1, 0, 190);
            }

            Label label = new Label("准备连接服务器...");
            ProgressBar bar = new JFXProgressBar();
            VBox vBox = new VBox(label, bar);

            Task.runCached(() -> {
                try {
                    Thread.sleep(180);
                    Task.runInUIThread(() -> layout.setBody(vBox));
                    //连接服务器
                    ServerConnect connect = new RemoteConnector(label, bar, builder);
                    connect.connect(ip, port, password);
                } catch (Exception e) {
                    Draw.drawErr(e);
                }
            });
        }
    }

    public static class AddLocalServer implements EventHandler<ActionEvent> {
        private final JFXDialogLayout layout;
        public AddLocalServer(JFXDialogLayout layout) {
            this.layout = layout;
        }

        @Override
        public void handle(ActionEvent event) {
            for (Node node : layout.getBody()) {
                AnimationUtil.takeXTranslateAnimation(node, node.getLayoutX(), -50, 190);
                AnimationUtil.takeFadeAnimation(node, 1, 0, 190);
            }

            Task.delayRunInUIThread(() -> {

            }, 190);
        }
    }
}
