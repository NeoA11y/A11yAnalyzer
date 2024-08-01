package com.neoa11y.analyzer.example.compose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neoa11y.analyzer.example.compose.R
import com.neoa11y.analyzer.example.compose.ui.theme.A11yAnalyzerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) = Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
        TopAppBar(
            title = {
                Text(stringResource(R.string.app_name))
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorScheme.primary,
                titleContentColor = colorScheme.onPrimary
            )
        )
    }
) {

    Column(
        modifier = Modifier
            .padding(it)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var showText by remember { mutableStateOf(true) }

        if (showText) {
            Text(
                text = "Hello, World!",
                modifier = Modifier.padding(
                    bottom = 8.dp
                )
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