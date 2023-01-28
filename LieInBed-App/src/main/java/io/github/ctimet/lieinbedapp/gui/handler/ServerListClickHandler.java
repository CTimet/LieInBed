package io.github.ctimet.lieinbedapp.gui.handler;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.jfoenix.controls.*;
import io.github.ctimet.lieinbedapp.gui.Draw;
import io.github.ctimet.lieinbedapp.gui.animation.AnimationUtil;
import io.github.ctimet.lieinbedapp.gui.util.DialogBuilder;
import io.github.ctimet.lieinbedapp.gui.util.SVGUtil;
import io.github.ctimet.lieinbedapp.gui.util.Task;
import io.github.ctimet.lieinbedapp.properties.AppProperties;
import io.github.ctimet.lieinbedapp.server.Server;
import io.github.ctimet.lieinbedapp.server.ServerConnect;
import io.github.ctimet.lieinbedapp.server.impl.RemoteServerConnect;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerListClickHandler implements EventHandler<ActionEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ServerListClickHandler.class);
    private DialogBuilder chooseServerDialog;
    private JFXDialogLayout layout;
    @Override
    public void handle(ActionEvent event) {
        chooseServerDialog = new DialogBuilder(Draw.pane)
                .setTitle("选择一个服务器")
                .addCancelButton(Color.BLACK, null)
                .addOKButton(Color.BLACK, null);
        VBox content = new VBox();

        LinkedList<JFXButton> buttons = new LinkedList<>();
        Server.getServers().forEach((k, v) -> {
            JFXButton button = new JFXButton(k);
            button.setOnAction(this::chooseServer);
            buttons.add(button);
        });

        JFXButton add = new JFXButton("+ 添加新的服务器");
        add.setOnAction(addAction -> {
            layout = chooseServerDialog.getLayout();
            for (Node node : layout.getBody()) {
                AnimationUtil.takeXTranslateAnimation(node, node.getLayoutX(), -50, 190);
                AnimationUtil.takeFadeAnimation(node, 1, 0, 190);
            }

            //在同一时间我们还要准备要入场的新组件
            VBox serverType = new VBox();
            JFXButton localServer = new JFXButton("+ 本地服务器");
            localServer.setOnAction(this::addLocalServer);
            JFXButton remoteServer = new JFXButton("+ 远程服务器");
            remoteServer.setOnAction(this::addRemoteServer);
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

    public void addRemoteServer(ActionEvent event) {
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

            chooseServerDialog.getOKButton().setOnAction(e -> {
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
                    connectServer(ip, port, password);
                }
            });

            AnimationUtil.takeXTranslateAnimation(vBox, 200, vBox.getLayoutX(), 190);
            AnimationUtil.takeFadeAnimation(vBox, 0, 1.0, 190);
        }, 190);
    }

    public void addLocalServer(ActionEvent event) {

    }

    public void chooseServer(ActionEvent event) {
        //能够发生此事件的对象一定是JFXButton
        String click = ((JFXButton) event.getSource()).getText();

    }

    public void connectServer(String ip, int port, String password) {
        //这里是UI代码
        JFXProgressBar bar = new JFXProgressBar();
        Label label = new Label("准备连接服务器");
        VBox box = new VBox(label, bar);

        AnimationUtil.takeJFXLayoutIOTranslation (
                layout,
                box
        );

        //这里是异步代码
        Task.runFixed(() -> {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, port), Integer.parseInt(AppProperties.getProperty("connect.timeout")));
                Task.runInUIThread(() -> {
                    label.setText("正在建立连接...");
                    bar.setProgress(0.16);
                });
                try (InputStream input = socket.getInputStream();
                     OutputStream output = socket.getOutputStream();
                     Scanner in = new Scanner(
                             input,
                             AppProperties.getProperty("connect.scanner.encoding")
                     );
                     PrintStream out = new PrintStream(
                             output,
                             AppProperties.getProperty("connect.printstream.autoflush").equals("true"),
                             AppProperties.getProperty("connect.printstream.encoding")
                     )) {
                    //第一步，发送连接密码
                    out.println(password);
                    Task.runInUIThread(() -> {
                        label.setText("校验密码...");
                        logger.info("校验密码...");
                        bar.setProgress(0.25);
                    });
                    if (in.hasNextLine() && in.nextLine().equals("PWW")) {
                        logger.info("密码错误, INPUT: {}", password);//密码错误就可以把password log出来
                        Task.runInUIThread(() -> {
                            addRemoteServer(null);
                            Draw.drawWarn("密码错误！");
                        });
                        return;
                    }

                    logger.info("正在加密通信");
                    Task.runInUIThread(() -> label.setText("加密通信..."));
                    //生成私钥和公钥
                    RSA rsa = new RSA();
                    //发送公钥
                    out.println(rsa.getPublicKeyBase64());
                    Task.runInUIThread(() -> bar.setProgress(0.51));

                    //解密发来的对称密钥
                    if (!in.hasNextLine()) return;
                    byte[] key = rsa.decrypt(in.nextLine(), KeyType.PrivateKey);
                    Task.runInUIThread(() -> bar.setProgress(0.77));
                    
                    //构建ServerConnect
                    ServerConnect connect = new RemoteServerConnect(key, in, out);
                    Task.runInUIThread(() -> {
                        JFXTextField nameInput = new JFXTextField();
                        nameInput.setPromptText("设置服务器名称");
                        chooseServerDialog.getOKButton().setOnAction(e -> {
                            String serverName;
                            if (nameInput.getText().equals("")) {
                                Draw.drawWarn("未配置服务器名称，LieInBed已经自动生成。");
                                serverName = "服务器 " + (Server.getServers().size() + 1);
                            } else {
                                serverName = nameInput.getText();
                            }

                            Server.addServer(serverName, connect);
                            chooseServerDialog.getAlert().hideWithAnimation();

                            Draw.box.clearWithAnimation()
                                    .addTopButton(
                                            "控制   ",
                                            null,
                                            new ServerConsoleHandler(connect)
                                    )
                                    .addTopButton(
                                            "性能   ",
                                            null,
                                            new ServerPerformanceHandler(connect)
                                    )
                                    .addTopButton(
                                            "配置   ",
                                            null,
                                            new ServerPerformanceHandler(connect)
                                    )
                                    .addTopButton(
                                            "玩家   ",
                                            null,
                                            new ServerPlayerHandler(connect)
                                    )
                                    .addDownButton(
                                            "",
                                            SVGUtil.back(Bindings.createObjectBinding(() -> Color.WHITE), 1, 1),
                                            event -> {
                                                Draw.box.clearWithAnimation();
                                                Draw.drawButtonsBox(Draw.box);
                                            })
                                    .init();
                        });
                        AnimationUtil.takeJFXLayoutIOTranslation(
                                layout,
                                nameInput
                        );
                    });
                    connect.init();
                }
            } catch (UnknownHostException e) {
                Task.runInUIThread(() -> {
                    Draw.drawWarn("未知的主机！");
                    addRemoteServer(null);
                });
            } catch (ConnectException e) {
                Task.runInUIThread(() -> {
                    Draw.drawWarn("连接异常！\n" + e.getMessage());
                    addRemoteServer(null);
                });
            } catch (SocketException e) {
                Task.runInUIThread(() -> {
                    Draw.drawWarn("套接字出现问题！\n" + e.getMessage());
                    addRemoteServer(null);
                });
            } catch (IOException e) {
                Task.runInUIThread(() -> {
                    Draw.drawWarn("连接发生IO异常！\n" + e.getMessage());
                    addRemoteServer(null);
                    Draw.drawErr(e);
                });
            }
        });
    }
}
