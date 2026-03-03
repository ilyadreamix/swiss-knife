package io.gitlab.ilyadreamix.swissknife.bottomsheet

import android.app.Dialog
import android.graphics.Color
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.setViewTreeOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

@Composable
internal actual fun SKBottomSheetContainer(content: @Composable () -> Unit) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val viewModelStoreOwner = LocalViewModelStoreOwner.current
  val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current
  val onBackPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current

  val compositionContext = rememberCompositionContext()

  val dialog = remember {
    Dialog(context).apply {
      window?.apply {
        setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        setDimAmount(0f)

        WindowCompat.setDecorFitsSystemWindows(this, false)

        @Suppress("DEPRECATION")
        statusBarColor = Color.TRANSPARENT
        @Suppress("DEPRECATION")
        navigationBarColor = Color.TRANSPARENT
      }
    }
  }

  val composeView = remember {
    ComposeView(context).apply {
      setParentCompositionContext(compositionContext)

      setViewTreeLifecycleOwner(lifecycleOwner)
      setViewTreeViewModelStoreOwner(viewModelStoreOwner)
      setViewTreeSavedStateRegistryOwner(savedStateRegistryOwner)

      if (onBackPressedDispatcherOwner != null) {
        setViewTreeOnBackPressedDispatcherOwner(onBackPressedDispatcherOwner)
      }

      setContent { content() }
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
