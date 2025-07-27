package com.neoa11y.analyzer

import android.graphics.Rect
import android.view.Window
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.descendants
import com.neoa11y.analyzer.core.Analyzer
import com.neoa11y.analyzer.core.Node
import com.neoa11y.analyzer.core.content

internal class ViewAnalyzer : Analyzer {
    override fun invoke(window: Window): List<Node> {

        return window.content.descendants.mapNotNull {

            val node = AccessibilityNodeInfoCompat.obtain()

            it.onInitializeAccessibilityNodeInfo(node.unwrap())

            if (!node.isImportantForAccessibility) {
                return@mapNotNull null
            }

            if (!node.isVisibleToUser) {
                return@mapNotNull null
            }

            val rect = Rect().apply {
                node.getBoundsInScreen(this)
            }

            Node(
                it.id,
                rect.left.toFloat(),
                rect.top.toFloat(),
                rect.width(),
                rect.height(),
                node.text.toString()
            )
        }.toList()
    }
}