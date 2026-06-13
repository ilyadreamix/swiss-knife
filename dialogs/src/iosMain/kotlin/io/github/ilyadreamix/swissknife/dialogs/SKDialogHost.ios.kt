package io.github.ilyadreamix.swissknife.dialogs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent

@Composable
actual fun SKDialogHost(
  onBack: () -> Boolean,
  systemUIOptions: SKDialogHostSystemUIOptions,
  content: @Composable () -> Unit
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

  SKDialogHostStatusBarStyleEffect(systemUIOptions.statusBarIconsStyle)
}

@Composable
private fun SKDialogHostStatusBarStyleEffect(style: SKDialogHostSystemUIOptions.SystemBarIconsStyle) {

  val isSystemDark = isSystemInDarkTheme()
  val key = remember { Any() }

  DisposableEffect(style, isSystemDark) {
    val uiStyle = when (style) {
      SKDialogHostSystemUIOptions.SystemBarIconsStyle.Light -> UIStatusBarStyleLightContent
      SKDialogHostSystemUIOptions.SystemBarIconsStyle.Dark -> UIStatusBarStyleDarkContent
      SKDialogHostSystemUIOptions.SystemBarIconsStyle.Automatic ->
        if (isSystemDark) UIStatusBarStyleLightContent else UIStatusBarStyleDarkContent
    }

    SKStatusBarManager.push(key, uiStyle)

    onDispose {
      SKStatusBarManager.pop(key)
    }
  }
}
