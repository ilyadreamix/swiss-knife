package io.gitlab.ilyadreamix.swissknife.dialogs.bottomsheet

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos

@Composable
internal fun rememberSKBottomSheetState(
  visible: Boolean,
  animationSpec: AnimationSpec<Float>? = null,
  dismissThreshold: Float = 0.4f,
) = remember { SKBottomSheetState(visible, animationSpec, dismissThreshold) }

@Stable
internal class SKBottomSheetState(
  visible: Boolean,
  private val animationSpec: AnimationSpec<Float>?,
  private val dismissThreshold: Float
) {

  private val _visible = mutableStateOf(visible)
  val visible get() = _visible.value

  private val _animationProgress = Animatable(if (visible) 1f else 0f)
  val animationProgress get() = _animationProgress.value

  private var height = 0f
  fun setHeight(height: Float) { this.height = height }

  var totalDragAmount = 0f
    private set

  suspend fun show() {
    _visible.value = true
    withFrameNanos { /* ... */ }
    animateTo(1f)
  }

  suspend fun hide() {
    animateTo(0f)
    _visible.value = false
  }

  fun onDragStart() { totalDragAmount = 0f }

  suspend fun onDragCancel() { animateTo(1f) }

  suspend fun onDrag(amount: Float) {
    val currentHeight = height
    if (height > 0f) {
      totalDragAmount += amount
      val progress = 1f - (totalDragAmount / currentHeight).coerceAtLeast(0f)
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

  private suspend fun animateTo(value: Float) {
    animationSpec
      ?.let { _animationProgress.animateTo(value, it) }
      ?: _animationProgress.animateTo(value)
  }
}
