package io.gitlab.ilyadreamix.swissknife.dialogs.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.gitlab.ilyadreamix.swissknife.dialogs.SKDialogHost

@Composable
fun SKBottomSheet(visible: Boolean, onHide: () -> Unit, content: @Composable () -> Unit) {
  if (visible) {
    SKDialogHost {
      Box(modifier = Modifier.fillMaxSize()) {
        content()
      }
    }
  }
}
