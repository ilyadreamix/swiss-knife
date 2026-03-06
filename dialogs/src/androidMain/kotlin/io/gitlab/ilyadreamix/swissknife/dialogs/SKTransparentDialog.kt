package io.gitlab.ilyadreamix.swissknife.dialogs

import android.app.Dialog
import android.content.Context
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.core.view.WindowCompat

internal data class SKTransparentDialogSystemUIOptions(
  val isAppearanceLightStatusBars: Boolean,
  val isAppearanceLightNavigationBars: Boolean,
)

internal class SKTransparentDialog(
  context: Context,
  private val onBack: () -> Boolean,
  private val systemUIOptions: SKTransparentDialogSystemUIOptions
) : Dialog(context, R.style.SKTransparentDialog) {

  init { setCancelable(false) }

  override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      return onBack()
    } else {
      return super.onKeyUp(keyCode, event)
    }
  }

  override fun onStart() {
    super.onStart()

    window?.apply {
      setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
      setWindowAnimations(-1)

      WindowCompat.enableEdgeToEdge(this)
      WindowCompat.getInsetsController(this, decorView).apply {
        isAppearanceLightStatusBars = systemUIOptions.isAppearanceLightStatusBars
        isAppearanceLightNavigationBars = systemUIOptions.isAppearanceLightNavigationBars
      }
    }
  }
}
