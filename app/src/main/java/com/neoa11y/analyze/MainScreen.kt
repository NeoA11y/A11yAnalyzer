package com.neoa11y.analyze

import android.content.Intent
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neoa11y.analyze.ui.theme.A11yAnalyzerTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) = Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier.fillMaxSize()
) {

    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current
            ?.onBackPressedDispatcher

    IconButton(
        onClick = {
            onBackPressedDispatcher?.onBackPressed()
        }
    ) {
        Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "voltar"
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        var showText by remember { mutableStateOf(true) }

        if (showText) {
            Text("Compose")
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

    val context = LocalContext.current

    IconButton(
        onClick = {
            context.startActivity(
                Intent(
                    context, ViewActivity::class.java
                )
            )
        }
    ) {
        Icon(
            Icons.Filled.ArrowForward,
            contentDescription = "avançar"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    A11yAnalyzerTheme {
        MainScreen()
    }
}