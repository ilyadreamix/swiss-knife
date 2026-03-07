package io.github.ilyadreamix.swissknife.dialogs

import androidx.compose.runtime.Composable

/**
 * Controls the appearance of the system UI while the dialog is visible.
 *
 * @param statusBarIconsStyle Appearance of the status bar icons.
 * @param navigationBarIconsStyle Appearance of the navigation bar icons.
 */
data class SKDialogHostSystemUIOptions(
  val statusBarIconsStyle: SystemBarIconsStyle = SystemBarIconsStyle.Automatic,
  val navigationBarIconsStyle: SystemBarIconsStyle = SystemBarIconsStyle.Automatic,
) {
  /**
   * Controls whether system bar icons (status bar, navigation bar) appear light or dark.
   *
   * @see SystemBarIconsStyle.Light
   * @see SystemBarIconsStyle.Dark
   * @see SystemBarIconsStyle.Automatic
   */
  enum class SystemBarIconsStyle {
    /** Forces light (white) icons, suitable for dark backgrounds. */
    Light,
    /** Forces dark (black) icons, suitable for light backgrounds. */
    Dark,
    /** Follows the current system theme: light icons in the dark theme, dark icons in the light theme. */
    Automatic;
  }
}

/**
 * A platform-implemented full-sized transparent overlay container used internally by Swiss Knife
 * dialog components (e.g. [io.github.ilyadreamix.swissknife.dialogs.alert.SKAlert]).
 * It renders [content] above the main UI, intercepts back gestures,
 * and optionally adjusts system UI while visible.
 *
 * ## Usage
 * `SKDialogHost` should be wrapped in an `if` block. When the condition becomes `false`,
 * the host is removed from the composition immediately – with no animations.
 * Any exit transitions must be handled by the [content] itself before the condition flips:
 * ```
 * if (isVisible) {
 *   SKDialogHost(...) {
 *     // Content lives here
 *   }
 * }
 * ```
 *
 * @param onBack Invoked when the system back button or back gesture is triggered.
 *   Return `true` to consume the event, `false` to let it propagate up the back stack.
 * @param systemUIOptions Controls the appearance of the system UI while the host is active.
 * @param content The composable content rendered inside the overlay.
 */
@Composable
expect fun SKDialogHost(
  onBack: () -> Boolean,
  systemUIOptions: SKDialogHostSystemUIOptions = SKDialogHostSystemUIOptions(),
  content: @Composable () -> Unit
)
