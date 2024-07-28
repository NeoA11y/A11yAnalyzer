package com.neoa11y.analyzer.extension

import android.view.View
import android.view.ViewGroup

fun View.asViewGroup(): ViewGroup {
    return this as ViewGroup
}
