@file:SuppressLint("VisibleForTests")

package com.neoa11y.analyzer.compose

import android.annotation.SuppressLint
import android.view.Window
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getAllSemanticsNodes
import androidx.compose.ui.semantics.getOrNull
import com.neoa11y.analyzer.compose.extension.firstOrNull
import com.neoa11y.analyzer.core.Analyzer
import com.neoa11y.analyzer.core.Node
import com.neoa11y.analyzer.core.content

internal class ComposeAnalyzer : Analyzer {

    override fun invoke(window: Window): List<Node> {

        val androidComposeView = window.content.firstOrNull<ViewRootForTest>()!!

        return androidComposeView.semanticsOwner.getAllSemanticsNodes(
            mergingEnabled = true
        ).filterNot {
            it.config.getOrNull(SemanticsProperties.Text).isNullOrEmpty()
        }.map { node ->

            val rect = node.boundsInRoot

            Node(
                node.id,
                rect.left,
                rect.top,
                rect.width.toInt(),
                rect.height.toInt(),
                node.config[SemanticsProperties.Text].first().text
            )
        }
    }
}