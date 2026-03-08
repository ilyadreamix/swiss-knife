package io.github.ilyadreamix.swissknife.example

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import io.github.ilyadreamix.swissknife.example.core.SKExamplesScaffold

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
      SKExamplesScaffold()
    }
  }
}
