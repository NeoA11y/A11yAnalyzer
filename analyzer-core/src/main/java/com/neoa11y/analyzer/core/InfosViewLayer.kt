package com.neoa11y.analyzer.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class InfosViewLayer(
    context: Context,
) : View(context), ViewLayer, LifecycleOwner {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val lifecycleRegistry = LifecycleRegistry(this)

    override val lifecycle = lifecycleRegistry

    var selected by Delegates.observable<Int?>(null) { _, _, _ ->
        invalidate()
    }

    var nodes by Delegates.observable(listOf<Node>()) { _, _, _ ->
        invalidate()
    }

    private var active by Delegates.observable(false) { _, _, _ ->
        invalidate()
    }

    init {
        tag = TAG

        setupListeners()
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
