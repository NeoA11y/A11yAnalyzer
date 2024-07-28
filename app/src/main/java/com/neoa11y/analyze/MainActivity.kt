package com.neoa11y.analyze

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.isVisible
import com.neoa11y.analyze.databinding.ActivityMainBinding
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

class ViewActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {

        binding.btnForward.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnClickMe.setOnClickListener {
            binding.tvText.isVisible = !binding.tvText.isVisible
        }
    }
}