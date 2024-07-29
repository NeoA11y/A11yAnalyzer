package com.neoa11y.analyzer.compose.extension

import android.view.ViewGroup
import androidx.core.view.descendants

inline fun <reified T> ViewGroup.firstOrNull(): T? {

    for (child in descendants) {
        if (child is T) {
            return child
        }
    }

    return null
}