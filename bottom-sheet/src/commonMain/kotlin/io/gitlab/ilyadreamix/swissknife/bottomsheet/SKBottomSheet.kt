package io.gitlab.ilyadreamix.swissknife.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SKBottomSheet(visible: Boolean, onHide: () -> Unit, content: @Composable () -> Unit) {
  if (visible) {
    SKBottomSheetContainer {
      Box(modifier = Modifier.fillMaxSize()) {
        content()
      }
    }
  }
}
