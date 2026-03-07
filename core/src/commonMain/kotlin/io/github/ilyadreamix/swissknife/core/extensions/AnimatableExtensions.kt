package io.github.ilyadreamix.swissknife.core.extensions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector

suspend fun <T, V : AnimationVector> Animatable<T, V>.animate(target: T, spec: AnimationSpec<T>?) {
  if (spec == null) {
    animateTo(target)
  } else {
    animateTo(target, spec)
  }
}
