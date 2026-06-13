package io.github.ilyadreamix.swissknife.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
actual fun SKDialogHost(
  onBack: () -> Boolean,
  systemUIOptions: SKDialogHostSystemUIOptions,
  content: @Composable (() -> Unit)
) {
  Dialog(
    onDismissRequest = { onBack() },
    properties = @OptIn(ExperimentalComposeUiApi::class) DialogProperties(
      dismissOnClickOutside = false,
      dismissOnBackPress = false,
      useSoftwareKeyboardInset = false,
      usePlatformInsets = false,
      usePlatformDefaultWidth = false,
      scrimColor = Color.Transparent,
      animateTransition = false
    ),
    content = content
  )
}
