package com.neoa11y.analyzer.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup

@SuppressLint("StaticFieldLeak")
data class ActivityWatcher(
    val application: Application,
    val analyzer: Analyzer
) {

    private var current: Activity? = null
    private var infos: InfosViewLayer? = null
    private var controls: ControlsViewLayer? = null

    fun install() {

        if (installed) return

        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {

                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) = Unit

                override fun onActivityStarted(activity: Activity) = Unit

                override fun onActivityResumed(activity: Activity) {
                    install(activity)
                    analyzer(activity)
                }

                override fun onActivityPaused(activity: Activity) {
                    val decorView = activity.window.decorView as ViewGroup

                    decorView.removeView(infos)
                    infos = null
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
        val content = activity.window.content

        infos = content.findViewWithTag(InfosViewLayer.TAG)
        controls = decorView.findViewWithTag(ControlsViewLayer.TAG)

        if (infos == null) {
            infos = InfosViewLayer(activity)

            content.addView(infos)
        }

        if (controls == null) {
            controls = ControlsViewLayer(activity)

            decorView.addView(controls)
        }
    }

    private fun analyzer(activity: Activity) {
        activity
            .window
            .decorView
            .viewTreeObserver.addOnDrawListener {
                infos?.nodes = analyzer(activity.window)
            }
    }

    companion object {
        private var installed = false
    }
}