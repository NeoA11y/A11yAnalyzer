package com.neoa11y.analyzer.extension

import android.annotation.SuppressLint
import android.view.Window
import androidx.compose.ui.platform.ViewRootForTest
import com.neoa11y.analyzer.ComposeNodeFactory
import com.neoa11y.analyzer.Node
import com.neoa11y.analyzer.ViewBasedNodeFactory

@SuppressLint("VisibleForTests")
fun Window.getNodes(): List<Node> {

    val viewGroup = decorView
        .asViewGroup()

    val androidComposeView = viewGroup
        .firstOrNull<ViewRootForTest>()

    if (androidComposeView != null) {
        return ComposeNodeFactory.create(androidComposeView)
    }

    return ViewBasedNodeFactory.create(viewGroup)
}