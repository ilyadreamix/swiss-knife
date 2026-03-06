package io.gitlab.ilyadreamix.swissknife.example.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.gitlab.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheet
import io.gitlab.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetContainer
import io.gitlab.ilyadreamix.swissknife.dialogs.bottomsheet.SKBottomSheetInsets

@Composable
internal fun SKMaterial3BottomSheet(
  visible: Boolean,
  onHide: () -> Unit,
  insets: SKBottomSheetInsets,
  content: @Composable () -> Unit,
) {
  SKBottomSheet(
    visible = visible,
    onHide = { onHide() },
    insets = insets,
    container = SKBottomSheetContainer(
      maxWidth = 640.dp,
      color = MaterialTheme.colorScheme.surfaceContainerLow,
      shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ),
    content = {
      CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Box(
            modifier = Modifier
              .align(Alignment.CenterHorizontally)
              .padding(vertical = 22.dp)
              .height(4.dp)
              .width(32.dp)
              .background(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(50)
              )
          )

          content()
        }
      }
    }
  )
}
