package io.github.ilyadreamix.swissknife.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
actual fun SKDialogHost(
  onBack: () -> Boolean,
  systemUIOptions: SKDialogHostSystemUIOptions,
  content: @Composable (() -> Unit)
) {
  Popup(
    alignment = Alignment.Center,
    onDismissRequest = { onBack() },
    properties = PopupProperties(
      focusable = true,
      dismissOnBackPress = false,
      dismissOnClickOutside = false,
      clippingEnabled = false,
      usePlatformDefaultWidth = false,
      usePlatformInsets = false
    ),
    content = content
  )
}
