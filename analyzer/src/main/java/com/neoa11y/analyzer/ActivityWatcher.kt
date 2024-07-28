package com.neoa11y.analyzer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import androidx.compose.ui.node.RootForTest
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.semantics.getAllSemanticsNodes
import androidx.core.view.descendants

operator fun AccessibilityNodeInfo.iterator() =
    object : Iterator<AccessibilityNodeInfo> {

        private var index = 0
        override fun hasNext() = index < childCount
        override fun next() = getChild(index++) ?: throw IndexOutOfBoundsException()

    }

val AccessibilityNodeInfo.children: Sequence<AccessibilityNodeInfo>
    get() = object : Sequence<AccessibilityNodeInfo> {
        override fun iterator() = this@children.iterator()
    }

internal class TreeIterator<T>(
    rootIterator: Iterator<T>,
    private val getChildIterator: ((T) -> Iterator<T>)
) : Iterator<T> {
    private val stack = mutableListOf<Iterator<T>>()

    private var iterator: Iterator<T> = rootIterator

    override fun hasNext(): Boolean {
        return iterator.hasNext()
    }

    override fun next(): T {
        val item = iterator.next()
        prepareNextIterator(item)
        return item
    }

    private fun prepareNextIterator(item: T) {
        val childIterator = getChildIterator(item)

        if (childIterator.hasNext()) {
            stack.add(iterator)
            iterator = childIterator
        } else {
            while (!iterator.hasNext() && stack.isNotEmpty()) {
                iterator = stack.last()
                stack.removeLast()
            }
        }
    }
}

val AccessibilityNodeInfo.descendants: Sequence<AccessibilityNodeInfo>
    get() = Sequence {
        TreeIterator(children.iterator()) { child ->
            child.children.iterator()
        }
    }

@SuppressLint("StaticFieldLeak")
data class ActivityWatcher(val application: Application) {

    private var current: Activity? = null
    private var nodes: List<Node> = emptyList()
    private var overlay: ViewOverlay? = null

    init {
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
            }
        )
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

            val composeView = decorView.descendants.firstInstance<ViewRootForTest>()

            nodes = composeView.semanticsOwner.getAllSemanticsNodes(
                mergingEnabled = true
            ).map {
                val rect = it.boundsInWindow

                Node(
                    rect.left,
                    rect.top,
                    rect.width.toInt(),
                    rect.height.toInt()
                )
            }

            draw()
        }
    }

    private fun draw() {
        if (overlay?.nodes == nodes) return

        overlay?.nodes = nodes
        overlay?.invalidate()
    }
}

inline fun <reified T> Sequence<*>.firstInstance(): T {
    val iterator = iterator()

    while (iterator.hasNext()) {
        val item = iterator.next()

        if (item is T) {
            return item
        }
    }

    throw NoSuchElementException(
        "Sequence contains no element matching the predicate."
    )
}
