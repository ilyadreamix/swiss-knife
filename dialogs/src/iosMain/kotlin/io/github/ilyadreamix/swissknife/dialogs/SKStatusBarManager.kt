package io.github.ilyadreamix.swissknife.dialogs

import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.setStatusBarStyle

internal object SKStatusBarManager {

  private var initialStyle: UIStatusBarStyle? = null
  private val stack = mutableListOf<Pair<Any, UIStatusBarStyle>>()

  fun push(key: Any, style: UIStatusBarStyle) {
    if (initialStyle == null) {
      initialStyle = UIApplication.sharedApplication.statusBarStyle
    }
    stack.removeAll { it.first == key }
    stack.add(key to style)
    applyTop()
  }

  fun pop(key: Any) {
    stack.removeAll { it.first == key }
    applyTop()
  }

  private fun applyTop() {
    val styleToApply = stack.lastOrNull()?.second ?: initialStyle ?: return
    UIApplication.sharedApplication.setStatusBarStyle(styleToApply, animated = true)
    if (stack.isEmpty()) {
      initialStyle = null
    }
  }
}
