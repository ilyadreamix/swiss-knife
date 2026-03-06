package io.gitlab.ilyadreamix.swissknife.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.gitlab.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheet
import io.gitlab.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetBehavior
import io.gitlab.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetContainer
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

      var visible by remember { mutableStateOf(false) }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(100.dp)
          .background(Color.Red).clickable { visible = true }
      ) {
        BasicText(text = "Click me!", modifier = Modifier)
      }

      SKBottomSheet(
        visible = visible,
        onHide = { visible = false },
        container = SKBottomSheetContainer(
          color = Color.White,
          shape = androidx.compose.foundation.shape.RoundedCornerShape(
            topStart = 16.dp, topEnd = 16.dp
          ),
          maxWidth = 480.dp
        ),
        behavior = SKBottomSheetBehavior(animationSpec = tween(300)),
        content = {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState())
          ) {
            Box(modifier = Modifier.height(16.dp))

            repeat(100) {
              BasicText(text = "Item $it", modifier = Modifier.padding(horizontal = 16.dp))
            }

            Box(modifier = Modifier.height(16.dp))
          }
        }
      )
    }
  }
}
