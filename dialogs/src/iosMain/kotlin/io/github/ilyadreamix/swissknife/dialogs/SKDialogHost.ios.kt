package io.github.ilyadreamix.swissknife.dialogs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent

@Composable
actual fun SKDialogHost(
  onBack: () -> Boolean,
  systemUIOptions: SKDialogHostSystemUIOptions,
  content: @Composable () -> Unit
) {

  val viewController = LocalUIViewController.current

  val isSystemDark = isSystemInDarkTheme()
  val statusBarStyle = remember(isSystemDark, systemUIOptions.statusBarIconsStyle) {
    when (systemUIOptions.statusBarIconsStyle) {
      SKDialogHostSystemUIOptions.SystemBarIconsStyle.Light ->
        UIStatusBarStyleLightContent
      SKDialogHostSystemUIOptions.SystemBarIconsStyle.Dark ->
        UIStatusBarStyleDarkContent
      SKDialogHostSystemUIOptions.SystemBarIconsStyle.Automatic ->
        if (isSystemDark) UIStatusBarStyleLightContent else UIStatusBarStyleDarkContent
    }
  }

  DisposableEffect(viewController, statusBarStyle) {
    val composeViewController = ComposeUIViewController(content)
    val dialogViewController = SKTransparentDialogViewController(composeViewController, statusBarStyle)

    viewController.presentViewController(dialogViewController, animated = false, completion = null)

    onDispose {
      dialogViewController.dismissViewControllerAnimated(false, completion = null)
    }
  }
}
