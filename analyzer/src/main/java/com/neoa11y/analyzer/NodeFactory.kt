package com.neoa11y.analyzer

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.semantics.getAllSemanticsNodes
import androidx.core.view.descendants

interface NodeFactory<T> {
    fun create(root: T): List<Node>
}

object ViewBasedNodeFactory : NodeFactory<ViewGroup> {

    override fun create(root: ViewGroup): List<Node> {

        return root.descendants.map {

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
}

@SuppressLint("VisibleForTests")
object ComposeNodeFactory : NodeFactory<ViewRootForTest> {

    override fun create(root: ViewRootForTest): List<Node> {

        return root.semanticsOwner.getAllSemanticsNodes(
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
}