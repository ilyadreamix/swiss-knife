package io.gitlab.ilyadreamix.swissknife.dialogs

import android.view.ViewGroup
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
internal actual fun SKDialogHost(onBack: () -> Boolean, content: @Composable () -> Unit) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val viewModelStoreOwner = LocalViewModelStoreOwner.current
  val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current

  val compositionContext = rememberCompositionContext()

  val dialog = remember { SKTransparentDialog(context, onBack) }
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
