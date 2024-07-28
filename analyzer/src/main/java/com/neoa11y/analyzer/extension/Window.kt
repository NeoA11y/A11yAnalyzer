package com.neoa11y.analyzer.extension

import android.view.Window
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.semantics.getAllSemanticsNodes
import androidx.core.view.descendants
import com.neoa11y.analyzer.Node

fun Window.getNodes(): List<Node> {

    val viewGroup = decorView
        .asViewGroup()

    val androidComposeView = viewGroup
        .firstOrNull<ViewRootForTest>()

    if (androidComposeView != null) {

        // compose based

        return androidComposeView.semanticsOwner.getAllSemanticsNodes(
            mergingEnabled = true
        ).map {
            val rect = it.boundsInWindow

            Node(
                rect.left,
                rect.top,
                rect.width.toInt(),
                rect.height.toInt()
            )
        }
    }

    // view based

    return viewGroup.descendants.map {

        val location = IntArray(2)

        it.getLocationOnScreen(location)

        Node(
            location[0].toFloat(),
            location[1].toFloat(),
            it.width,
            it.height
        )
    }.toList()
}