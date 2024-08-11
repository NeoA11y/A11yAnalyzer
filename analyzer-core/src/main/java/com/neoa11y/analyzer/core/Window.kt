package com.neoa11y.analyzer.core

import android.view.ViewGroup
import android.view.Window

val Window.content: ViewGroup get() = decorView.findViewById(android.R.id.content)