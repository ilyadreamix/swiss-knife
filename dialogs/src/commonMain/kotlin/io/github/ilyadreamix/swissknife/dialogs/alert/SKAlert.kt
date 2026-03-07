package io.github.ilyadreamix.swissknife.dialogs.alert

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import io.github.ilyadreamix.swissknife.core.SKInsets
import io.github.ilyadreamix.swissknife.core.padding
import io.github.ilyadreamix.swissknife.dialogs.SKDialogHost
import io.github.ilyadreamix.swissknife.dialogs.SKDialogHostSystemUIOptions

/**
 * Describes the reason why the alert was requested to hide.
 *
 * Passed to [SKAlert]'s `onHide` callback so the caller can distinguish
 * between different user interactions and react accordingly.
 *
 * @see SKAlertHideReason.Back
 * @see SKAlertHideReason.TouchOutside
 */
enum class SKAlertHideReason {
  /** The system back button or back gesture was triggered. */
  Back,
  /** The user tapped the scrim area outside the alert. */
  TouchOutside;
}

/**
 * Controls which gestures and system events are allowed to hide the alert.
 *
 * @param hideOnTouchOutside Whether tapping the scrim area dismisses the alert.
 * @param hideOnBackPress Whether the system back press dismisses the alert.
 *   Set to `false` if you are handling back navigation externally (e.g. via Decompose).
 */
data class SKAlertHideOptions(
  val hideOnTouchOutside: Boolean = true,
  val hideOnBackPress: Boolean = true,
)

/**
 * An alert dialog component with an ability to animate show/hide transitions.
 *
 * ## Callbacks
 *
 * ### `onHide`
 * Called when the user performs an action that *requests* the alert to close –
 * a back press or a tap on the scrim. This callback does **not** hide
 * the alert by itself; the caller is responsible for updating the `visible` flag:
 * ```
 * SKAlert(
 *   visible = isVisible,
 *   onHide = { isVisible = false } // Caller drives visibility
 * )
 * ```
 *
 * ### `onHidden`
 * Called **after** the hide animation fully completes. Use this to release
 * resources that should stay alive during the closing animation – for example,
 * destroying a Decompose child component only after the alert has visually disappeared:
 * ```
 * SKAlert(
 *   visible = state.isAlertVisible,
 *   onHide = { component.hideAlert() },      // Sets isAlertVisible = false
 *   onHidden = { component.onAlertHidden() } // Navigation.dismiss() lives here
 * ) {
 *   child.instance?.let { AlertContent(it) }
 * }
 * ```
 * Note: if `visible` switches back to `true` while the closing animation is still
 * running, the animation coroutine is cancelled and `onHidden` is **not** invoked –
 * this is intentional, as the previous hidden state is no longer relevant.
 *
 * @param visible Whether the alert should be shown. Changing this value triggers
 *   the corresponding show or hide animation.
 * @param onHide Invoked when the user requests the alert to close. See above.
 * @param hideOptions Controls which gestures are allowed to trigger [onHide].
 * @param onHidden Invoked once the hide animation has fully completed. See above.
 * @param systemUIOptions Controls system bar icons appearance while the alert is shown.
 *   The default parameter keeps in mind that the scrim is dark by default,
 *   so it uses light icons for both status and navigation bars.
 * @param insets Window insets applied as padding around the alert content.
 *   Pass [SKInsets.Zero] to disable inset handling.
 * @param animationSpec Custom animation spec for show/hide transitions.
 *   Set this to `null` to use the default animation,
 *   defined in [androidx.compose.animation.core.Animatable].
 * @param scrimColor Color of the background overlay behind the alert.
 * @param alignment Alignment of the alert within the screen.
 *   Defaults to [Alignment.Center].
 * @param content The composable content rendered inside the alert.
 *   Receives the current `animationProgress` (from `0f` to `1f`) so the content
 *   can animate its own appearance in sync with the show/hide transition.
 */
@Composable
fun SKAlert(
  visible: Boolean,
  onHide: (SKAlertHideReason) -> Unit,
  hideOptions: SKAlertHideOptions = SKAlertHideOptions(),
  onHidden: (() -> Unit)? = null,
  systemUIOptions: SKDialogHostSystemUIOptions = SKDialogHostSystemUIOptions(
    statusBarIconsStyle = SKDialogHostSystemUIOptions.SystemBarIconsStyle.Light,
    navigationBarIconsStyle = SKDialogHostSystemUIOptions.SystemBarIconsStyle.Light,
  ),
  insets: SKInsets = SKInsets(WindowInsets.safeDrawing),
  animationSpec: AnimationSpec<Float>? = null,
  scrimColor: Color = Color.Black.copy(alpha = 0.5f),
  alignment: Alignment = Alignment.Center,
  content: @Composable (Float) -> Unit,
) {

  val state = rememberSKAlertState(visible, animationSpec)

  val animationProgress = state.animationProgress
  val finalScrimColor = scrimColor.copy(alpha = scrimColor.alpha * animationProgress)

  if (state.visible) {
    SKDialogHost(
      onBack = {
        if (hideOptions.hideOnBackPress) {
          onHide(SKAlertHideReason.Back)
          true
        } else {
          false
        }
      },
      systemUIOptions = systemUIOptions
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .drawBehind { drawRect(finalScrimColor) }
          .pointerInput(hideOptions.hideOnTouchOutside) {
            detectTapGestures {
              if (hideOptions.hideOnTouchOutside) {
                onHide(SKAlertHideReason.TouchOutside)
              }
            }
          },
        contentAlignment = alignment,
      ) {
        Box(
          modifier = Modifier
            .wrapContentSize()
            .padding(insets)
            .pointerInput(Unit) {
              detectTapGestures { /* ... */ }
            },
          content = { content(animationProgress) },
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
