package com.neoa11y.analyzer.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import kotlin.properties.Delegates

class InfosViewLayer(
    context: Context,
) : View(context), ViewLayer {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    var nodes by Delegates.observable(listOf<Node>()) { _, _, _ ->
        invalidate()
    }

    init {
        tag = TAG
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        nodes.forEach { node ->
            canvas.drawRect(
                node.x,
                node.y,
                node.x + node.width,
                node.y + node.height,
                paint
            )
        }
    }

    companion object {
        const val TAG = "infos_view_layer"
    }
}
