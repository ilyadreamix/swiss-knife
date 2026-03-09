package io.github.ilyadreamix.swissknife.dialogs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent

@Composable
actual fun SKDialogHost(
  onBack: () -> Boolean,
  systemUIOptions: SKDialogHostSystemUIOptions,
  content: @Composable () -> Unit
) {
  // The reasons behind using default Popup:
  //
  // 1. The solution with creating UIViewController
  // with UIModalPresentationOverFullScreen makes view background solid instead
  // of transparent. Also, it seems to be that composition local providers won't work with
  // this newly created UIViewController.
  //
  // 2. Under the hood (in skikoMain), Popup uses a thing called rememberComposeSceneLayer.
  // I don't exactly know how does it work, and I also can't try to use it because
  // it is internal.

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
