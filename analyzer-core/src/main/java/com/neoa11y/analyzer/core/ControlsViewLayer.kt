package com.neoa11y.analyzer.core

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.neoa11y.analyzer.core.databinding.ControlsBinding
import kotlinx.coroutines.launch

class ControlsViewLayer(
    context: Context,
) : FrameLayout(context), ViewLayer, LifecycleOwner {

    private val binding = ControlsBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val lifecycleRegistry = LifecycleRegistry(this)

    override val lifecycle = lifecycleRegistry

    init {
        setupDragAndDrop()
        applyWindowInsets()
        setupView()
        setupObservers()

        tag = TAG
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    private fun setupObservers() = lifecycleScope.launch {
        repeatOnLifecycle(
            Lifecycle.State.STARTED
        ) {
            context.dataStore.data.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect {
                binding.ibToggle.isActivated =
                    it[booleanPreferencesKey("active")] ?: false

                binding.ibTouchApp.isActivated =
                    it[booleanPreferencesKey("active_interaction")] ?: false
            }
        }
    }

    private fun setupView() {

        binding.ibToggle.setOnClickListener {
            lifecycleScope.launch {
                context.dataStore.edit {
                    it[booleanPreferencesKey("active")] = !binding.ibToggle.isActivated
                }
            }
        }

        binding.ibTouchApp.setOnClickListener {
           lifecycleScope.launch {
                context.dataStore.edit {
                    it[booleanPreferencesKey("active_interaction")] = !binding.ibTouchApp.isActivated
                }
            }
        }
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