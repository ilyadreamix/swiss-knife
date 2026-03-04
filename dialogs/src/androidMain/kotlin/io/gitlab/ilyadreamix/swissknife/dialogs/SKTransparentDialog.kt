package io.gitlab.ilyadreamix.swissknife.dialogs

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import androidx.core.view.WindowCompat

internal class SKTransparentDialog(context: Context)
  : Dialog(context, R.style.SKTransparentDialog) {

  override fun onStart() {
    super.onStart()

    window?.apply {
      setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
      setWindowAnimations(-1)

      WindowCompat.enableEdgeToEdge(this)
      WindowCompat.getInsetsController(this, decorView).apply {
        isAppearanceLightStatusBars = false
        isAppearanceLightNavigationBars = false
      }
    }
  }
}
