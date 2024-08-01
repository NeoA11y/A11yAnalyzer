package com.neoa11y.analyzer.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.neoa11y.analyzer.example.compose.ui.theme.A11yAnalyzerTheme
import com.neoa11y.analyzer.example.compose.ui.MainScreen

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