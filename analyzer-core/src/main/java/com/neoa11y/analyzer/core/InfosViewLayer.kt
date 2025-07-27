package com.neoa11y.analyzer.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.neoa11y.analyzer.core.databinding.DetailsBinding
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class InfosViewLayer(
    context: Context,
) : FrameLayout(context), ViewLayer, LifecycleOwner {

    private val details = DetailsBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val view = object : View(context) {
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            if (!active) return

            nodes.forEach { node ->
                paint.color = if (node.id == selected) {
                    Color.RED
                } else {
                    Color.BLUE
                }
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

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val lifecycleRegistry = LifecycleRegistry(this)

    override val lifecycle = lifecycleRegistry

    var selected by Delegates.observable<Int?>(null) { _, _, new ->
        invalidate()
        children.forEach { it.invalidate() }
        details.root.isVisible = new != null
        details.text.text = nodes.find { it.id == new }?.text
    }

    var nodes by Delegates.observable(listOf<Node>()) { _, _, _ ->
        invalidate()
        children.forEach { it.invalidate() }
    }

    var active by Delegates.observable(false) { _, _, new ->
        isVisible = new
    }

    init {
        tag = TAG

        setupListeners()

        addView(
            view,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )

        details.root.isVisible = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    private fun setupListeners() = lifecycleScope.launch {
        context.dataStore.data.flowWithLifecycle(
            lifecycle,
            Lifecycle.State.STARTED
        ).collect {
            active = it[booleanPreferencesKey("active")] ?: false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (active && event.action == MotionEvent.ACTION_DOWN) {
            selected = nodes.find { node ->
                event.x >= node.x &&
                        event.x <= node.x + node.width &&
                        event.y >= node.y &&
                        event.y <= node.y + node.height
            }?.id

            return true
        }

        return super.onTouchEvent(event)
    }

    companion object {
        const val TAG = "infos_view_layer"
    }
}
