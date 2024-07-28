package com.neoa11y.analyzer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.allViews

@SuppressLint("StaticFieldLeak")
object ActivityWatcher {

    private var current: Activity? = null
    private var nodes: List<Node> = emptyList()
    private var overlay: ViewOverlay? = null

    fun install(application: Application) {

        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {

                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) {
                    install(activity)
                }

                override fun onActivityStarted(activity: Activity) = Unit

                override fun onActivityResumed(activity: Activity) {
                    analyzer(activity)
                }

                override fun onActivityPaused(activity: Activity) = Unit

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
            })
    }

    private fun install(activity: Activity) {
        overlay = ViewOverlay(activity)

        val decorView = activity.window.decorView as ViewGroup

        val found = decorView.findViewWithTag<ViewOverlay>("overlay")

        if (found != null) {
            overlay = found
            return
        }

        decorView.addView(
            overlay,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun analyzer(activity: Activity) {

        val decorView = activity.window.decorView as ViewGroup

        decorView.viewTreeObserver.addOnGlobalLayoutListener {

            nodes = decorView.allViews.map { view ->
                val location = IntArray(2)

                view.getLocationOnScreen(location)

                val x = location[0].toFloat()
                val y = location[1].toFloat()

                Node(x, y, view.width, view.height)
            }.toList()

            draw()
        }
    }

    private fun draw() {
        if (overlay?.nodes == nodes) return

        overlay?.nodes = nodes
        overlay?.invalidate()
    }
}