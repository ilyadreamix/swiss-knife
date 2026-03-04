package io.gitlab.ilyadreamix.swissknife.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.text.BasicText
import io.gitlab.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheet

internal class SKActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge(
      navigationBarStyle = SystemBarStyle.auto(
        lightScrim = Color.TRANSPARENT,
        darkScrim = Color.TRANSPARENT
      ),
    )

    setContent {
      SKBottomSheet(
        visible = true,
        onHide = {},
        content = { BasicText(text = "Hello, world!") }
      )
    }
  }
}
