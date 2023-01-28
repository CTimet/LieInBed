package io.github.ctimet.lieinbedapp.gui.ui;

import io.github.ctimet.lieinbedapp.gui.animation.AnimationUtil;
import io.github.ctimet.lieinbedapp.gui.util.CSSStyle;
import io.github.ctimet.lieinbedapp.gui.util.SVGUtil;
import io.github.ctimet.lieinbedapp.gui.util.Task;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public class WarnList extends VBox {
    private final ObservableList<Node> content = FXCollections.observableArrayList();
    private final int space;
    private final int layoutX;

    public WarnList(int eachSpace, int x, int ly) {
        this.space = eachSpace;
        this.layoutX = x;
        this.setLayoutY(ly);
    }

    public final void showWarn(@NotNull String warn) {
        Intrinsics.checkNotNullParameter(warn, "warn");
        final VBox box = new VBox();
        Label title = new Label("Warning");
        title.setGraphic(SVGUtil.warning(Bindings.createObjectBinding(() -> Color.RED), 1.0D, 1.0D));
        title.setTextFill(Color.RED);
        Label message = new Label(warn);
        box.getChildren().addAll(title, message);
        CSSStyle.addDefaultStyle(box, "vBox");
        this.setSpacing(this.space);
        box.setSpacing(10.0D);
        box.setPadding(new Insets(5.0D, 10.0D, 5.0D, 10.0D));
        box.setPrefWidth(207.0D);
        this.content.add(box);
        this.getChildren().add(box);
        AnimationUtil.takeXTranslateAnimation(box, 1085.0D, this.layoutX, 300);
        Task.delayRunInUIThread(() -> {
            AnimationUtil.takeXTranslateAnimation(box, box.getBoundsInParent().getMinX(), 1085.0D, 180);
            Task.delayRunInUIThread(() -> WarnList.this.getChildren().remove(box), 320);
        }, 4000);
    }
}
