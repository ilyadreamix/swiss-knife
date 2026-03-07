package io.github.ilyadreamix.swissknife.example

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import io.github.ilyadreamix.swissknife.core.SKInsets
import io.github.ilyadreamix.swissknife.example.composables.SKMaterial3Alert
import io.github.ilyadreamix.swissknife.example.composables.SKMaterial3BottomSheet

internal class SKActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val isDarkTheme =
      (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    WindowCompat.enableEdgeToEdge(window)

    val wic = WindowInsetsControllerCompat(window, window.decorView)
    wic.isAppearanceLightStatusBars = !isDarkTheme
    wic.isAppearanceLightNavigationBars = !isDarkTheme

    setContent {

      val layoutDirection = LocalLayoutDirection.current

      val insetsPadding = WindowInsets.safeDrawing.asPaddingValues()
      val insetsPaddingTop = insetsPadding.calculateTopPadding()
      val insetsPaddingBottom = insetsPadding.calculateBottomPadding()
      val insetsPaddingStart = insetsPadding.calculateLeftPadding(layoutDirection)
      val insetsPaddingEnd = insetsPadding.calculateRightPadding(layoutDirection)

      val systemDark = isSystemInDarkTheme()
      val colorScheme = when {
        systemDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicDarkColorScheme(this)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicLightColorScheme(this)
        systemDark -> darkColorScheme()
        else -> lightColorScheme()
      }

      var sheetVisible by remember { mutableStateOf(false) }
      var alertVisible by remember { mutableStateOf(false) }

      MaterialTheme(colorScheme) {
        Scaffold(
          topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            CenterAlignedTopAppBar(
              title = {
                Text(text = "Swiss Knife Examples")
              }
            )
          }
        ) { innerPadding ->
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Button(
              onClick = { sheetVisible = true },
              content = { Text(text = "Show bottom sheet") }
            )

            Button(
              onClick = { alertVisible = true },
              content = { Text(text = "Show alert") }
            )
          }
        }

        SKMaterial3BottomSheet(
          visible = sheetVisible,
          onHide = { sheetVisible = false },
          insets = SKInsets(
            top = insetsPaddingTop,
            start = insetsPaddingStart,
            end = insetsPaddingEnd,
            bottom = 0.dp
          )
        ) {
          LazyColumn(
            modifier = Modifier
              .fillMaxWidth()
              .height(540.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
          ) {
            items(
              count = 100,
              key = { it }
            ) { index ->
              Text(
                text = "Item ${index + 1}",
                textAlign = TextAlign.Center,
              )
            }

            item(key = "insets") {
              Box(modifier = Modifier.height(insetsPaddingBottom))
            }
          }
        }

        SKMaterial3Alert(
          visible = alertVisible,
          onHide = { alertVisible = false },
          title = { Text(text = "Headline") },
          text = { Text(text = LoremIpsum(48).values.joinToString(" ")) },
          hideButton = {
            Button(
              onClick = { alertVisible = false },
              content = { Text(text = "Close") }
            )
          }
        )
      }
    }
  }
}
