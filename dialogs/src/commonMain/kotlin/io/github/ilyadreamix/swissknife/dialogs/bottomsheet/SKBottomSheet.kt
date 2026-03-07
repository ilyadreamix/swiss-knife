package io.github.ilyadreamix.swissknife.dialogs.bottomsheet

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isSpecified
import io.github.ilyadreamix.swissknife.core.SKInsets
import io.github.ilyadreamix.swissknife.dialogs.SKDialogHost
import io.github.ilyadreamix.swissknife.dialogs.SKDialogHostSystemUIOptions
import kotlinx.coroutines.launch

/**
 * Describes the reason why the bottom sheet was requested to hide.
 *
 * Passed to [SKBottomSheet]'s `onHide` callback so the caller can distinguish
 * between different user interactions and react accordingly.
 *
 * @see SKBottomSheetHideReason.Back
 * @see SKBottomSheetHideReason.Drag
 * @see SKBottomSheetHideReason.TouchOutside
 */
enum class SKBottomSheetHideReason {
  /** The system back button or back gesture was triggered. */
  Back,
  /** The user dragged the sheet past the hide threshold. */
  Drag,
  /** The user tapped the scrim area outside the sheet. */
  TouchOutside;
}

/**
 * Controls which gestures and system events are allowed to hide the bottom sheet.
 *
 * @param hideOnTouchOutside Whether tapping the scrim area dismisses the sheet.
 * @param hideOnDrag Whether dragging the sheet downward can dismiss it.
 * @param hideOnDragThreshold Fraction of the sheet height the user must drag before it dismisses.
 *   Value between `0f` (dismiss immediately) and `1f` (drag the entire sheet height).
 * @param hideOnBackPress Whether the system back press dismisses the sheet.
 *   Set to `false` if you are handling back navigation externally (e.g. via Decompose).
 */
data class SKBottomSheetHideOptions(
  val hideOnTouchOutside: Boolean = true,
  val hideOnDrag: Boolean = true,
  val hideOnDragThreshold: Float = DefaultHideOnDragThreshold,
  val hideOnBackPress: Boolean = true,
) {
  init {
    require(hideOnDragThreshold in 0f..1f) { "Parameter \"hideOnDragThreshold\" must be between 0f and 1f" }
  }

  companion object {
    const val DefaultHideOnDragThreshold = 0.4f
  }
}


/**
 * Visual container configuration for the bottom sheet surface.
 *
 * @param maxWidth Maximum width of the sheet. Use [Dp.Unspecified] to fill the full screen width.
 * @param color Background color of the sheet surface.
 * @param shape Shape applied to the sheet surface.
 * @param elevation Elevation (shadow) of the sheet. Use [Dp.Unspecified] to disable the elevation.
 */
data class SKBottomSheetContainer(
  val maxWidth: Dp = Dp.Unspecified,
  val color: Color,
  val shape: Shape,
  val elevation: Dp = Dp.Unspecified
)

/**
 * Controls animation and scroll behavior of the bottom sheet.
 *
 * @param animationSpec Custom animation spec for show/hide transitions.
 *   Set this to `null` to use the default animation,
 *   defined in [androidx.compose.animation.core.Animatable].
 * @param nestedScroll Whether the sheet participates in nested scrolling.
 *   When `true`, scrollable content inside the sheet can be dragged to dismiss it
 *   once the scroll reaches the top.
 */
data class SKBottomSheetBehavior(
  val animationSpec: AnimationSpec<Float>? = null,
  val nestedScroll: Boolean = true,
)

/**
 * A bottom sheet component with animated show/hide transitions, drag-to-dismiss,
 * nested scroll support, and a scrim overlay.
 *
 * ## Callbacks
 *
 * ### `onHide`
 * Called when the user performs an action that *requests* the sheet to close –
 * a back press, a drag, or a tap on the scrim. This callback does **not** hide
 * the sheet by itself; the caller is responsible for updating the `visible` flag:
 * ```
 * SKBottomSheet(
 *   visible = isVisible,
 *   onHide = { isVisible = false } // Caller drives visibility
 * )
 * ```
 *
 * ### `onHidden`
 * Called **after** the hide animation fully completes. Use this to release
 * resources that should stay alive during the closing animation – for example,
 * destroying a Decompose child component only after the sheet has visually disappeared:
 * ```
 * SKBottomSheet(
 *   visible = state.isSheetVisible,
 *   onHide = { component.hideSheet() },      // Sets isSheetVisible = false
 *   onHidden = { component.onSheetHidden() } // Navigation.dismiss() lives here
 * ) {
 *   child.instance?.let { SheetContent(it) }
 * }
 * ```
 * Note: if `visible` switches back to `true` while the closing animation is still
 * running, the animation coroutine is cancelled and `onHidden` is **not** invoked –
 * this is intentional, as the previous hidden state is no longer relevant.
 *
 * @param visible Whether the sheet should be shown. Changing this value triggers
 *   the corresponding show or hide animation.
 * @param onHide Invoked when the user requests the sheet to close. See above.
 * @param container Visual configuration of the sheet surface.
 * @param hideOptions Controls which gestures are allowed to trigger [onHide].
 * @param behavior Controls optional bottom sheet behavior.
 * @param scrimColor Color of the background overlay behind the sheet.
 * @param onHidden Invoked once the hide animation has fully completed. See above.
 * @param insets Window insets applied to the sheet surface.
 *   Top, start, and end insets are applied as padding **outside** the sheet background,
 *   capping its maximum size within safe bounds. Bottom inset is applied **inside**
 *   the sheet background, so the content is pushed up above the navigation bar
 *   while the background itself extends to the screen edge.
 *   Pass [SKInsets.Zero] to disable inset handling.
 * @param systemUIOptions Controls system bar icons appearance while the sheet is shown.
 *   The default parameter keeps in mind that the scrim is dark by default,
 *   so it uses light status bar icons.
 * @param content The composable content rendered inside the sheet.
 */
@Composable
fun SKBottomSheet(
  visible: Boolean,
  onHide: (SKBottomSheetHideReason) -> Unit,
  container: SKBottomSheetContainer,
  hideOptions: SKBottomSheetHideOptions = SKBottomSheetHideOptions(),
  behavior: SKBottomSheetBehavior = SKBottomSheetBehavior(),
  scrimColor: Color = Color.Black.copy(alpha = 0.5f),
  onHidden: (() -> Unit)? = null,
  insets: SKInsets = SKInsets(WindowInsets.safeDrawing),
  systemUIOptions: SKDialogHostSystemUIOptions = SKDialogHostSystemUIOptions(statusBarIconsStyle = SKDialogHostSystemUIOptions.SystemBarIconsStyle.Light),
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
      },
      systemUIOptions = systemUIOptions,
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
    }
  }

  LaunchedEffect(visible) {
    if (visible) {
      state.show()
    } else {
      state.hide()
      onHidden?.invoke()
    }
  }
}
