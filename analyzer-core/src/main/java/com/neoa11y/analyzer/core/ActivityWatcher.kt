package com.neoa11y.analyzer.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.ViewGroup

@SuppressLint("StaticFieldLeak")
data class ActivityWatcher(
    val application: Application,
    val analyzer: Analyzer
) {

    private var current: Activity? = null
    private var nodes: List<Node> = emptyList()
    private var overlay: ViewOverlay? = null

    fun install() {

        if (installed) return

        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {

                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) = Unit

                override fun onActivityStarted(activity: Activity) {
                    install(activity)
                }

                override fun onActivityResumed(activity: Activity) {
                    analyzer(activity)
                }

                override fun onActivityPaused(activity: Activity) {
                    val decorView = activity.window.decorView as ViewGroup

                    decorView.removeView(overlay)
                }

                override fun onActivityStopped(activity: Activity) = Unit

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle
                ) = Unit

                override fun onActivityDestroyed(activity: Activity) {
                    if (current === activity) {
                        current = null
                    }
                }
            }
        )

        installed = true
    }

    private fun install(activity: Activity) {

        val decorView = activity.window.decorView as ViewGroup

        overlay = decorView.findViewWithTag("overlay")

        if (overlay != null) return

        overlay = ViewOverlay(activity)

        decorView.addView(
            overlay,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun analyzer(activity: Activity) {

        val decorView = activity.window.decorView

        decorView.viewTreeObserver.addOnDrawListener {

            nodes = analyzer(activity.window)

            draw()
        }
    }

    private fun draw() {
        if (overlay?.nodes == nodes) return

        overlay?.nodes = nodes
        overlay?.invalidate()
    }

    companion object {
        private var installed = false
    }
}