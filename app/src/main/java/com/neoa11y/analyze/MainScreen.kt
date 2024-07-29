package com.neoa11y.analyze

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neoa11y.analyze.ui.theme.A11yAnalyzerTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) = Box(
    contentAlignment = Alignment.Center,
    modifier = modifier.fillMaxSize()
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        var showText by remember { mutableStateOf(true) }

        if (showText) {
            Text("Hello, World!")
            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        Button(
            onClick = {
                showText = !showText
            }
        ) {
            Text("Button")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    A11yAnalyzerTheme {
        MainScreen()
    }
}