package io.gitlab.ilyadreamix.swissknife.example

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import io.gitlab.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetInsets
import io.gitlab.ilyadreamix.swissknife.example.composables.SKMaterial3BottomSheet
import android.graphics.Color as SdkColor

internal class SKActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge(
      navigationBarStyle = SystemBarStyle.auto(
        lightScrim = SdkColor.TRANSPARENT,
        darkScrim = SdkColor.TRANSPARENT
      ),
    )

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
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
              .padding(16.dp),
            contentAlignment = Alignment.Center
          ) {
            Button(
              onClick = { sheetVisible = true },
              content = { Text(text = "Show bottom sheet") }
            )
          }
        }

        SKMaterial3BottomSheet(
          visible = sheetVisible,
          onHide = { sheetVisible = false },
          insets = SKBottomSheetInsets(
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
      }
    }
  }
}
