package com.neoa11y.analyzer

import android.view.ViewGroup
import android.view.Window
import androidx.core.view.descendants
import com.neoa11y.analyzer.core.Analyzer
import com.neoa11y.analyzer.core.Node

internal class ViewAnalyzer : Analyzer {
    override fun invoke(window: Window): List<Node> {

        val decorView = window.decorView as ViewGroup

        return decorView.descendants.map {

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