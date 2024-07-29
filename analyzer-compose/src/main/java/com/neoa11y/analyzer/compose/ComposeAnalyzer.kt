package com.neoa11y.analyzer.compose

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.Window
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.semantics.getAllSemanticsNodes
import com.neoa11y.analyzer.core.Node
import com.neoa11y.analyzer.core.Analyzer
import com.neoa11y.analyzer.compose.extension.firstOrNull

@SuppressLint("VisibleForTests")
internal class ComposeAnalyzer : Analyzer {

    override fun invoke(window: Window): List<Node> {

        val viewGroup = window.decorView as ViewGroup

        val androidComposeView = viewGroup.firstOrNull<ViewRootForTest>()!!

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
}