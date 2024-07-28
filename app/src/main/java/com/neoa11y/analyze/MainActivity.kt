package com.neoa11y.analyze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.neoa11y.analyze.ui.theme.A11yAnalyzerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            A11yAnalyzerTheme {
                MainScreen()
            }
        }
    }
}