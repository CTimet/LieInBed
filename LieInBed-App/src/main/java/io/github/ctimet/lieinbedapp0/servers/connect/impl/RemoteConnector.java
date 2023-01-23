package io.github.ctimet.lieinbedapp0.servers.connect.impl;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXTextField;
import io.github.ctimet.lieinbedapp0.gui.Draw;
import io.github.ctimet.lieinbedapp0.gui.handler.ServerConsoleHandler;
import io.github.ctimet.lieinbedapp0.gui.handler.ServerListClickHandler;
import io.github.ctimet.lieinbedapp0.gui.handler.ServerPerformanceHandler;
import io.github.ctimet.lieinbedapp0.gui.handler.ServerPlayerHandler;
import io.github.ctimet.lieinbedapp0.gui.util.DialogBuilder;
import io.github.ctimet.lieinbedapp0.gui.util.SVGUtil;
import io.github.ctimet.lieinbedapp0.servers.Server;
import io.github.ctimet.lieinbedapp0.servers.connect.Connection;
import io.github.ctimet.lieinbedapp0.servers.connect.ServerConnect;
import io.github.ctimet.lieinbedapp0.task.Task;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;

public class RemoteConnector implements ServerConnect {
    private final Label label;
    private final ProgressBar bar;
    private final JFXAlert<String> alert;
    private final DialogBuilder builder;
    private Connection conn;

    private static final Logger logger = LoggerFactory.getLogger(RemoteConnector.class);

    public RemoteConnector(Label label, ProgressBar bar, DialogBuilder builder) {
        this.label = label;
        this.bar = bar;
        this.alert = builder.getAlert();
        this.builder = builder;
    }

//    @Override
    public void connect(String ip, int port, String password) {
        //这个方法一定要异步调用
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), 5000);
            Task.runInUIThread(() -> {
                label.setText("正在建立连接...");
                bar.setProgress(0.16);
            });
            try (InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream();
                 Scanner in = new Scanner(input, "UTF-8");
                 PrintStream out = new PrintStream(output, true, "UTF-8")) {
                out.println(password);
                Task.runInUIThread(() -> {
                    label.setText("校验密码...");
                    logger.info("校验密码...");
                    bar.setProgress(0.25);
                });
                if (in.hasNextLine() && in.nextLine().equals("PWW")) {
                    logger.info("密码错误, INPUT: {}", password);//密码错误就可以把password log出来
                    Task.runInUIThread(() -> {
                        new ServerListClickHandler.AddRemoteServer(builder).handle(null);
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
                //构建Connection对象
                conn = new Connection(new RemoteInformationStream(key, in, out));
                out.println("APP");
                //正式建立连接。
                Task.runInUIThread(() -> {
                    label.setText("连接建立完毕，正在初始化UI...");
                    bar.setProgress(0.98);
                    alert.hideWithAnimation();
                    draw();
                });
                conn.init();
            }
        } catch (SocketTimeoutException e) {
            Task.runInUIThread(() -> {
                Draw.drawWarn("连接超时！");
                new ServerListClickHandler.AddRemoteServer(builder).handle(null);
            });
        } catch (UnknownHostException e) {
            Task.runInUIThread(() -> {
                Draw.drawWarn("未知的主机！");
                new ServerListClickHandler.AddRemoteServer(builder).handle(null);
            });
        } catch (ConnectException e) {
            Task.runInUIThread(() -> {
                Draw.drawWarn("连接异常！\n" + e.getMessage());
                new ServerListClickHandler.AddRemoteServer(builder).handle(null);
            });
        } catch (SocketException e) {
            Task.runInUIThread(() -> {
                Draw.drawWarn("套接字出现问题！\n" + e.getMessage());
                new ServerListClickHandler.AddRemoteServer(builder).handle(null);
            });
        } catch (IOException e) {
            Task.runInUIThread(() -> {
                Draw.drawWarn("连接发生IO异常！\n" + e.getMessage());
                new ServerListClickHandler.AddRemoteServer(builder).handle(null);
                Draw.drawErr(e);
            });
        }
    }

    public void draw() {
        Server server = new Server(this);
        Task.delayRunInUIThread(() -> {
            JFXTextField nameInput = new JFXTextField();
            nameInput.setPromptText("服务器名称");
            new DialogBuilder(Draw.pane)
                    .addOKButton(Color.BLACK, (e, alert) -> {
                        String serverName;
                        if (nameInput.getText().equals("")) {
                            Draw.drawWarn("未配置服务器名称，LieInBed已自动生成。");
                            serverName = "服务器" + (Server.getServerList().size() + 1);
                        } else {
                            serverName = nameInput.getText();
                        }
                        server.setName(serverName);
                        Server.getServerList().add(server);
                        alert.hideWithAnimation();
                    })
                    .setBody(nameInput)
                    .setTitle("设置服务器名称")
                    .createAndShow();
        }, 240);//在240ms后才开始让腐竹设置服务器名称，这是为了给动画一个过度时间避免生硬。

        Draw.box.clearWithAnimation()
                .addTopButton(
                        "控制   ",
                        null,
                        new ServerConsoleHandler(server)
                )
                .addTopButton(
                        "性能   ",
                        null,
                        new ServerPerformanceHandler()
                )
                .addTopButton(
                        "配置   ",
                        null,
                        new ServerPerformanceHandler()
                )
                .addTopButton(
                        "玩家   ",
                        null,
                        new ServerPlayerHandler()
                )
                .addDownButton(
                        "",
                        SVGUtil.back(Bindings.createObjectBinding(() -> Color.WHITE), 1, 1),
                        event -> {
                            Draw.box.clearWithAnimation();
                            Draw.drawButtonsBox(Draw.box);
                        })
                .init();
    }


    @Override
    public void startServer() {

    }

    @Override
    public void stopServer() {

    }

    @Override
    public void restartServer() {

    }

    @Override
    public void send(String json) {
        conn.send(json);
    }

    @Override
    public void addListener(Connection.Listener listener) {

    }
}
