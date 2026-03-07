package io.github.ilyadreamix.swissknife.core

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Inset configuration applied to some components.
 *
 * Please refer to corresponding documentation for each component.
 */
data class SKInsets(
  val start: Dp = 0.dp,
  val top: Dp = 0.dp,
  val end: Dp = 0.dp,
  val bottom: Dp = 0.dp,
) {
  companion object {
    val Zero = SKInsets()
  }
}

/**
 * Constructs [SKInsets] from [WindowInsets].
 */
@Suppress("ComposableNaming")
@Composable
@ReadOnlyComposable
fun SKInsets(windowInsets: WindowInsets): SKInsets {

  val layoutDirection = LocalLayoutDirection.current

  val padding = windowInsets.asPaddingValues()
  val top = padding.calculateTopPadding()
  val bottom = padding.calculateBottomPadding()
  val start = padding.calculateLeftPadding(layoutDirection)
  val end = padding.calculateRightPadding(layoutDirection)

  return SKInsets(start, top, end, bottom)
}

@Suppress("NOTHING_TO_INLINE")
inline fun Modifier.padding(insets: SKInsets) =
  padding(insets.start, insets.top, insets.end, insets.bottom)
