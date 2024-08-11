package com.neoa11y.analyzer.core

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

@SuppressLint("ClickableViewAccessibility")
class DragHandleTouchListener(
    private val target: View,
    private val parent: View
) : View.OnTouchListener {

    private var initialX = 0f
    private var initialY = 0f

    override fun onTouch(view: View, event: MotionEvent): Boolean {

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = target.x - event.rawX
                initialY = target.y - event.rawY
                true
            }

            MotionEvent.ACTION_MOVE -> {
                target.x = (event.rawX + initialX).coerceIn(
                    minimumValue = 0f,
                    maximumValue = (parent.width - target.width).toFloat()
                )

                target.y = (event.rawY + initialY).coerceIn(
                    minimumValue = 0f,
                    maximumValue = (parent.height - target.height).toFloat()
                )

                true
            }

            else -> false
        }
    }
}