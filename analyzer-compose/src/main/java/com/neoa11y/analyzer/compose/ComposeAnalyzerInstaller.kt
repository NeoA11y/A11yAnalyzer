package com.neoa11y.analyzer.compose

import android.app.Application
import com.neoa11y.analyzer.ActivityWatcher
import com.neoa11y.analyzer.core.Installer

internal class ComposeAnalyzerInstaller : Installer() {

    override fun onCreate(): Boolean {

        val context = checkNotNull(context)
        val application = context.applicationContext as Application

        ActivityWatcher(
            application,
            ComposeAnalyzer()
        ).install()

        return true
    }
}
