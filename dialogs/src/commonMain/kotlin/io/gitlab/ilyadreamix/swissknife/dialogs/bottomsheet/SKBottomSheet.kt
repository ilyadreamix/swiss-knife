package io.gitlab.ilyadreamix.swissknife.dialogs.bottomsheet

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isSpecified
import io.gitlab.ilyadreamix.swissknife.dialogs.SKDialogHost
import kotlinx.coroutines.launch

enum class SKBottomSheetHideReason {
  Back,
  Drag,
  TouchOutside;
}

data class SKBottomSheetHideOptions(
  val hideOnTouchOutside: Boolean = true,
  val hideOnDrag: Boolean = true,
  val hideOnDragThreshold: Float = DefaultHideOnDragThreshold,
  val hideOnBackPress: Boolean = true,
) {
  companion object {
    const val DefaultHideOnDragThreshold = 0.4f
  }
}

data class SKBottomSheetContainer(
  val maxWidth: Dp = Dp.Unspecified,
  val color: Color,
  val shape: Shape,
  val padding: PaddingValues
)

data class SKBottomSheetBehavior(
  val animationSpec: AnimationSpec<Float>? = null,
  val nestedScroll: Boolean = true,
)

@Composable
fun SKBottomSheet(
  visible: Boolean,
  onHide: (SKBottomSheetHideReason) -> Unit,
  container: SKBottomSheetContainer,
  hideOptions: SKBottomSheetHideOptions = SKBottomSheetHideOptions(),
  behavior: SKBottomSheetBehavior = SKBottomSheetBehavior(),
  scrimColor: Color = Color.Black.copy(alpha = 0.5f),
  content: @Composable () -> Unit
) {
  val state = rememberSKBottomSheetState(visible, behavior.animationSpec, hideOptions.hideOnDragThreshold)
  val coroutineScope = rememberCoroutineScope()

  val nestedScrollConnection = if (behavior.nestedScroll) {
    remember(state, coroutineScope, onHide) {
      SKBottomSheetNestedScrollConnection(
        state = state,
        coroutineScope = coroutineScope,
        onHide = { onHide(SKBottomSheetHideReason.Drag) }
      )
    }
  } else {
    null
  }

  val animationProgress = state.animationProgress
  val finalScrimColor = scrimColor.copy(alpha = scrimColor.alpha * animationProgress)

  if (state.visible) {
    SKDialogHost(
      onBack = {
        if (hideOptions.hideOnBackPress) {
          onHide(SKBottomSheetHideReason.Back)
          true
        } else {
          false
        }
      }
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .drawBehind { drawRect(finalScrimColor) }
          .pointerInput(hideOptions.hideOnTouchOutside) {
            detectTapGestures {
              if (hideOptions.hideOnTouchOutside) {
                onHide(SKBottomSheetHideReason.TouchOutside)
              }
            }
          }
      ) {
        Box(
          content = { content() },
          modifier = Modifier
            .then(
              if (container.maxWidth.isSpecified) {
                Modifier.widthIn(max = container.maxWidth)
              } else {
                Modifier.fillMaxWidth()
              }
            )
            .wrapContentHeight()
            .align(Alignment.BottomCenter)
            .graphicsLayer { translationY = size.height * (1f - animationProgress) }
            .onSizeChanged { state.setHeight(it.height.toFloat()) }
            .background(container.color, container.shape)
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
            .padding(container.padding)
        )
      }
    }
  }

  LaunchedEffect(visible) {
    if (visible) {
      state.show()
    } else {
      state.hide()
    }
  }
}
