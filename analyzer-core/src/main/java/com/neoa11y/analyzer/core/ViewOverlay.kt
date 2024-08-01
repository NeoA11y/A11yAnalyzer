package com.neoa11y.analyzer.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class ViewOverlay(
    context: Context,
) : View(context) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    var nodes: List<Node> = emptyList()

    init {
        tag = "overlay"
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
}
