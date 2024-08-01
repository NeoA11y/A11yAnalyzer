package com.neoa11y.analyzer.example.interoperability

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.core.view.isVisible
import com.neoa11y.analyzer.example.interoperability.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        binding.button.setContent {
            Button(
                onClick = {
                    binding.tvText.isVisible = !binding.tvText.isVisible
                }
            ) {
                Text("Click Me")
            }
        }
    }
}