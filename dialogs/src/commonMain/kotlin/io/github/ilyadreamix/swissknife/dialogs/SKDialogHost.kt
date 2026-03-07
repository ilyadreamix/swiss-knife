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

@Composable
internal expect fun SKDialogHost(
  onBack: () -> Boolean,
  systemUIOptions: SKDialogHostSystemUIOptions = SKDialogHostSystemUIOptions(),
  content: @Composable () -> Unit
)
