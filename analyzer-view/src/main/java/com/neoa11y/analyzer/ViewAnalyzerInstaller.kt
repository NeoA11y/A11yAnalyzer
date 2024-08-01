package com.neoa11y.analyzer

import android.app.Application
import com.neoa11y.analyzer.core.ActivityWatcher
import com.neoa11y.analyzer.core.Installer

internal class ViewAnalyzerInstaller : Installer() {

    override fun onCreate(): Boolean {

        val context = checkNotNull(context)
        val application = context.applicationContext as Application

        ActivityWatcher(
            application,
            ViewAnalyzer()
        ).install()

        return true
    }
}
