package io.github.ctimet.lieinbedapp.gui.handler;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import io.github.ctimet.lieinbedapp.gui.util.AnimationUtil;
import io.github.ctimet.lieinbedapp.gui.util.DialogBuilder;
import io.github.ctimet.lieinbedapp.servers.Server;
import io.github.ctimet.lieinbedapp.task.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.LinkedList;

import static io.github.ctimet.lieinbedapp.gui.Draw.drawErr;

public class ServerListClickHandler implements EventHandler<ActionEvent> {
    private DialogBuilder chooseServerDialog;
    private final AnchorPane pane;
    public ServerListClickHandler(DialogBuilder chooseServerDialog, AnchorPane pane) {
        this.chooseServerDialog = chooseServerDialog;
        this.pane = pane;
    }

    @Override
    public void handle(ActionEvent event) {
        if (chooseServerDialog == null)
            chooseServerDialog = new DialogBuilder(pane)
                    .setTitle("选择一个服务器")
                    .addCancelButton(Color.BLACK, null);

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
            LinkedList<Node> waitRemove = new LinkedList<>();
            for (Node node : layout.getBody()) {
                AnimationUtil.takeXTranslateAnimation(node, node.getLayoutX(), -50, 190);
                AnimationUtil.takeFadeAnimation(node, 1, 0, 190);
                waitRemove.add(node);//每播放一个动画，就有一个组件将要被删除
            }

            //在同一时间我们还要准备要入场的新组件
            VBox serverType = new VBox();
            JFXButton localServer = new JFXButton("+ 本地服务器");
            localServer.setOnAction(le -> waitRemove.clear());
            JFXButton remoteServer = new JFXButton("+ 远程服务器");
            remoteServer.setOnAction(re -> waitRemove.clear());
            serverType.getChildren().addAll(localServer, remoteServer);

            Task.runInCachedPool(() -> {
                try {
                    //休眠方法不可以直接在action里执行，这会堵塞UI线程，应该另开一个线程执行休眠（等待动画播放结束）然后再在UI线程中操纵UI，防止卡死或者报错
                    Thread.sleep(190);
                    Task.runInUIThread(() -> {
                        layout.getBody().removeAll(waitRemove);//删除所有将要被删除的组件，而不是新加入的组件
                        waitRemove.clear();//清空以便下次使用

                        chooseServerDialog.getLayout().setHeading(new Label("选择新建服务器类型"));

                        layout.setBody(serverType);
                        //播放新组件的入场动画
                        AnimationUtil.takeXTranslateAnimation(serverType, 200, serverType.getLayoutX(), 190);
                        AnimationUtil.takeFadeAnimation(serverType, 0, 1.0, 190);
                        //播放动画需要在旧组件移除后再播放，否则会出现问题
                    });
                } catch (InterruptedException e) {
                    drawErr(e);
                }
            });
        });
        buttons.add(add);
        content.getChildren().addAll(buttons);

        chooseServerDialog.setBody(content).createAndShow();
    }
}
