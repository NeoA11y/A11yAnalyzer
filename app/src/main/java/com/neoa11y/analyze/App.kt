package com.neoa11y.analyze

import android.app.Application
import com.neoa11y.analyzer.ActivityWatcher

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        ActivityWatcher(this)
    }
}