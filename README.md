# 🔪 Swiss Knife

A Kotlin Multiplatform library for Compose Multiplatform with a set of UI utilities and components.

<table>
  <thead>
    <tr>
      <th>SKAlert</th>
      <th>SKBottomSheet</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><video src="https://github.com/user-attachments/assets/2b363ef0-0757-4761-b811-0e2b733ba4eb" height="500" autoplay loop muted></video></td>
      <td><video src="https://github.com/user-attachments/assets/044651d4-d6e3-40b1-8a1c-0b957a2eef57" height="500" autoplay loop muted></video></td>
    </tr>
  </tbody>
</table>

## Modules

| Module                  | Description                         |
|-------------------------|-------------------------------------|
| [`core`](core/)         | Foundation utilities and extensions |
| [`dialogs`](dialogs/)   | Dialogs and bottom sheets           |
| [`examples`](examples/) | Demonstration of the components     |

## Platform support

| Platform        | Status         |
|-----------------|----------------|
| Android         | ✅ Supported    |
| iOS             | ✅ Supported ([Note](#ios-note))    |
| Other platforms | ❌️ Not planned |

## Examples

The `examples/android` module contains a working Android app that demonstrates `SKAlert` and
`SKBottomSheet` integrated with Material 3 theming. Run it directly from Android Studio.

<a name="ios-note"></a>
## iOS note

In order to use some Swiss Knife features, you need to make changes to your `Info.plist` file:

### Set `UIViewControllerBasedStatusBarAppearance` to `NO`
Swiss Knife componentes based on `SKDialogHost` use `UIApplication.sharedApplication.setStatusBarStyle` method
to ensure that status bar appearance above the dark scrim is light by default. 

If you don't need this behavior, you can skip this step.

## License

This project is licensed under the terms of the [LICENSE](LICENSE) file included in the repository.
