# dialogs

Dialog components for Swiss Knife.

## Contents

- [SKAlert](#skalert)
- [SKBottomSheet](#skbottomsheet)

### SKAlert

An alert dialog with an ability to animate hide/show transitions. The caller fully
controls visibility – `SKAlert` never hides itself.

The `content` lambda receives `animationProgress` (`0f` → `1f`) so the content can animate its own
appearance in sync with the scrim and show/hide transitions.

```kotlin
var isVisible by remember { mutableStateOf(false) }

SKAlert(
  visible = isVisible,
  onHide = { isVisible = false },
) { animationProgress ->
  Surface(
    modifier = Modifier.graphicsLayer {
      alpha = animationProgress
      scaleX = lerp(0.85f, 1f, animationProgress)
      scaleY = lerp(0.85f, 1f, animationProgress)
    }
  ) {
    // Content
  }
}
```

### SKBottomSheet

A bottom sheet with animated slide-in/out, drag-to-dismiss, nested scroll support, and a dimmed
scrim. Same visibility contract as `SKAlert` – the caller owns the `visible` flag.

```kotlin
var isVisible by remember { mutableStateOf(false) }

SKBottomSheet(
  visible = isVisible,
  onHide = { isVisible = false },
  container = SKBottomSheetContainer(
    color = MaterialTheme.colorScheme.surface,
    shape = MaterialTheme.shapes.extraLarge.copy(
      bottomStart = CornerSize(0.dp),
      bottomEnd = CornerSize(0.dp),
    ),
  ),
) {
  // Content
}
```
