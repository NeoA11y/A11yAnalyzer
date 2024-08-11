package com.neoa11y.analyzer.core

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import com.neoa11y.analyzer.core.databinding.ControlsBinding

class ControlsViewLayer(
    context: Context,
) : FrameLayout(context), ViewLayer {

    private val binding = ControlsBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        setupDragAndDrop()
        applyWindowInsets()

        tag = TAG
    }

    private fun applyWindowInsets() {
        setOnApplyWindowInsetsListener { view, insets ->
            view.updateLayoutParams<MarginLayoutParams> {
                leftMargin = insets.systemWindowInsetLeft
                topMargin = insets.systemWindowInsetTop
                rightMargin = insets.systemWindowInsetRight
                bottomMargin = insets.systemWindowInsetBottom
            }
            insets
        }
    }

    private fun setupDragAndDrop() {

        var initialX = 0f
        var initialY = 0f

        binding.root.setOnTouchListener { view, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = view.x - event.rawX
                    initialY = view.y - event.rawY
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    view.x = (event.rawX + initialX).coerceIn(
                        minimumValue = 0f,
                        maximumValue = width.toFloat() - view.width
                    )

                    view.y = (event.rawY + initialY).coerceIn(
                        minimumValue = 0f,
                        maximumValue = height.toFloat() - view.height
                    )

                    true
                }

                else -> false
            }
        }
    }

    companion object {
        const val TAG = "controls_view_layer"
    }
}