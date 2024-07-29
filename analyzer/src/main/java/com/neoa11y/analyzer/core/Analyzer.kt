package com.neoa11y.analyzer.core

import android.view.Window

interface Analyzer {
    operator fun invoke(window: Window): List<Node>
}