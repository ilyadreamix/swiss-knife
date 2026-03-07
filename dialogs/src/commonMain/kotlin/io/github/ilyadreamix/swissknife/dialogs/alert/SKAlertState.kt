package io.github.ilyadreamix.swissknife.dialogs.alert

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import io.github.ilyadreamix.swissknife.core.extensions.animate

@Composable
internal fun rememberSKAlertState(visible: Boolean, animationSpec: AnimationSpec<Float>?) =
  remember { SKAlertState(visible, animationSpec) }

@Stable
internal class SKAlertState(visible: Boolean, private val animationSpec: AnimationSpec<Float>?) {

  private val _visible = mutableStateOf(visible)
  val visible get() = _visible.value

  private val _animationProgress = Animatable(if (visible) 1f else 0f)
  val animationProgress get() = _animationProgress.value

  suspend fun show() {
    _visible.value = true
    withFrameNanos { /* ... */ }
    _animationProgress.animate(1f, animationSpec)
  }

  suspend fun hide() {
    _animationProgress.animate(0f, animationSpec)
    _visible.value = false
  }
}
