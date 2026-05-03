package io.github.ilyadreamix.swissknife.dialogs.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.isSpecified
import io.github.ilyadreamix.swissknife.core.SKInsets
import kotlinx.coroutines.launch

/**
 * This is a default low-level composable that renders bottom sheet.
 *
 * You can use it to implement your own bottom sheet instead of using [SKBottomSheet].
 */
@Composable
fun SKBottomSheetContent(
  content: @Composable () -> Unit,
  state: SKBottomSheetState,
  container: SKBottomSheetContainer,
  hideOptions: SKBottomSheetHideOptions,
  insets: SKInsets,
  animationProgress: Float,
  nestedScrollConnection: NestedScrollConnection?,
  onHide: (SKBottomSheetHideReason) -> Unit,
  modifier: Modifier = Modifier
) {

  val coroutineScope = rememberCoroutineScope()

  Box(
    content = { content() },
    modifier = modifier
      .then(
        if (container.maxWidth.isSpecified) {
          Modifier.widthIn(max = container.maxWidth)
        } else {
          Modifier.fillMaxWidth()
        }
      )
      .wrapContentHeight()
      .padding(
        start = insets.start,
        end = insets.end,
        top = insets.top,
      )
      .graphicsLayer { translationY = size.height * (1f - animationProgress) }
      .then(
        if (container.elevation.isSpecified) {
          Modifier.shadow(container.elevation, container.shape)
        } else {
          Modifier
        }
      )
      .background(container.color, container.shape)
      .onSizeChanged { state.setHeight(it.height.toFloat()) }
      .pointerInput(Unit) {
        detectTapGestures { /* ... */ }
      }
      .then(if (nestedScrollConnection != null) Modifier.nestedScroll(nestedScrollConnection) else Modifier)
      .pointerInput(hideOptions.hideOnDrag) {
        if (!hideOptions.hideOnDrag) {
          return@pointerInput
        }

        detectVerticalDragGestures(
          onDragStart = { state.onDragStart() },
          onDragCancel = {
            coroutineScope.launch {
              state.onDragCancel()
            }
          },
          onVerticalDrag = { change, amount ->
            change.consume()
            coroutineScope.launch { state.onDrag(amount) }
          },
          onDragEnd = {
            coroutineScope.launch {
              val shouldHide = state.onDragEnd()
              if (shouldHide) {
                onHide(SKBottomSheetHideReason.Drag)
              }
            }
          }
        )
      }
      .padding(bottom = insets.bottom)
  )
}
