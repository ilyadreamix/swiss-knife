package io.github.ilyadreamix.swissknife.dialogs.bottomsheet

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import io.github.ilyadreamix.swissknife.core.extensions.animate

@Composable
fun rememberSKBottomSheetState(
  visible: Boolean,
  animationSpec: AnimationSpec<Float>? = null,
  dismissThreshold: Float = SKBottomSheetHideOptions.DefaultHideOnDragThreshold
) = remember { SKBottomSheetState(visible, animationSpec, dismissThreshold) }

@Stable
class SKBottomSheetState(
  visible: Boolean,
  private val animationSpec: AnimationSpec<Float>?,
  private val dismissThreshold: Float
) {

  private val _visible = mutableStateOf(visible)
  val visible get() = _visible.value

  private val _animationProgress = Animatable(if (visible) 1f else 0f)
  val animationProgress get() = _animationProgress.value

  var height = 0f

  var totalDragAmount = 0f
    private set

  suspend fun show() {
    _visible.value = true
    withFrameNanos { /* ... */ }
    _animationProgress.animate(1f, animationSpec)
  }

  suspend fun hide() {
    _animationProgress.animate(0f, animationSpec)
    _visible.value = false
  }

  fun onDragStart() { totalDragAmount = 0f }

  suspend fun onDragCancel() { _animationProgress.animate(1f, animationSpec) }

  suspend fun onDrag(amount: Float) {
    if (height > 0f) {
      totalDragAmount += amount
      val progress = 1f - (totalDragAmount / height).coerceAtLeast(0f)
      _animationProgress.snapTo(progress)
    }
  }

  suspend fun onDragEnd(): Boolean {
    val dragFraction = (totalDragAmount / height).coerceAtLeast(0f)
    val shouldHide = if (dragFraction >= dismissThreshold) {
      hide()
      true
    } else {
      onDragCancel()
      false
    }

    totalDragAmount = 0f

    return shouldHide
  }
}
