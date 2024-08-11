package com.neoa11y.analyzer.core

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDragAndDrop() {

        binding.root.setOnTouchListener(
            DragHandleTouchListener(binding.root, this)
        )
    }

    companion object {
        const val TAG = "controls_view_layer"
    }
}