package io.github.ctimet.lieinbedapp0.gui.ui

import io.github.ctimet.lieinbedapp0.gui.animation.AnimationUtil
import io.github.ctimet.lieinbedapp0.gui.util.CSSStyle
import io.github.ctimet.lieinbedapp0.gui.util.SVGUtil
import io.github.ctimet.lieinbedapp0.task.Task
import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

class WarnList(eachSpace: Int, x: Int, ly: Int) : VBox() {
    private val content: ObservableList<VBox> = FXCollections.observableArrayList()
    private val space = eachSpace
    private val layoutX = x

    init {
        layoutY = ly.toDouble()
    }

    fun showWarn(warn: String) {
        val box = VBox()
        val title = Label("Warning")
        title.graphic = SVGUtil.warning(
            Bindings.createObjectBinding(fun (): Paint {
                return Color.RED
            }), 1.0, 1.0
        )
        title.textFill = Color.RED
        val message = Label(warn)
        box.children.addAll(title, message)

        CSSStyle.addDefaultStyle(box, "vBox")
        spacing = space.toDouble()
        box.spacing = 10.0
        box.padding = Insets(5.0, 10.0, 5.0, 10.0)
        box.prefWidth = 207.0

        content.add(box)
        children.add(box)
        //执行动画
        AnimationUtil.takeXTranslateAnimation(box, 1085.0, layoutX.toDouble(), 300)
        Task.delayRunInUIThread({
            AnimationUtil.takeXTranslateAnimation(box, box.boundsInParent.minX, 1085.0, 180)
            Task.delayRunInUIThread({
                children.remove(box)
            }, 320)
        }, 4000)
    }
}