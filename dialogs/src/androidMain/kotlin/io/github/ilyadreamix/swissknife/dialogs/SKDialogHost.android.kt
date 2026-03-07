package io.github.ilyadreamix.swissknife.dialogs

import android.content.Context
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

@Composable
internal actual fun SKDialogHost(
  onBack: () -> Boolean,
  systemUIOptions: SKDialogHostSystemUIOptions,
  content: @Composable () -> Unit
) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val viewModelStoreOwner = LocalViewModelStoreOwner.current
  val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current

  val compositionContext = rememberCompositionContext()

  val isSystemDark = isSystemInDarkTheme()
  val dialog = remember { SKTransparentDialog(context, onBack, systemUIOptions, isSystemDark) }

  val composeView = remember {
    ComposeView(context).apply {
      setParentCompositionContext(compositionContext)

      setViewTreeLifecycleOwner(lifecycleOwner)
      setViewTreeViewModelStoreOwner(viewModelStoreOwner)
      setViewTreeSavedStateRegistryOwner(savedStateRegistryOwner)

      layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
      setContent(content)
    }
  }

  DisposableEffect(Unit) {
    dialog.setContentView(composeView)
    dialog.show()

    onDispose {
      dialog.dismiss()
      composeView.disposeComposition()
    }
  }
}

private fun SKTransparentDialog(
  context: Context,
  onBack: () -> Boolean,
  systemUIOptions: SKDialogHostSystemUIOptions,
  isSystemDark: Boolean
): SKTransparentDialog {
  val isAppearanceLightStatusBars = when (systemUIOptions.statusBarIconsStyle) {
    SKDialogHostSystemUIOptions.SystemBarIconsStyle.Light -> false
    SKDialogHostSystemUIOptions.SystemBarIconsStyle.Dark -> true
    SKDialogHostSystemUIOptions.SystemBarIconsStyle.Automatic -> !isSystemDark
  }

  val isAppearanceLightNavigationBars = when (systemUIOptions.navigationBarIconsStyle) {
    SKDialogHostSystemUIOptions.SystemBarIconsStyle.Light -> false
    SKDialogHostSystemUIOptions.SystemBarIconsStyle.Dark -> true
    SKDialogHostSystemUIOptions.SystemBarIconsStyle.Automatic -> !isSystemDark
  }

  val sktdSystemUIOptions = SKTransparentDialogSystemUIOptions(
    isAppearanceLightStatusBars = isAppearanceLightStatusBars,
    isAppearanceLightNavigationBars = isAppearanceLightNavigationBars,
  )

  return SKTransparentDialog(context, onBack, sktdSystemUIOptions)
}
