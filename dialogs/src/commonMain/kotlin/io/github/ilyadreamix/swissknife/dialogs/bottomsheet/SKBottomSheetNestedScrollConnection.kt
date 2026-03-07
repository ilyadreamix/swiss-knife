//
// Inspired by SheetNestedScrollConnection from dokar3/sheets:
// https://github.com/dokar3/sheets/blob/2c4ca1817cd7217cbdffe4a1c936596d9a37bcce/sheets-core/src/commonMain/kotlin/com/dokar/sheets/SheetNestedScrollConnection.kt
//

package io.github.ilyadreamix.swissknife.dialogs.bottomsheet

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class SKBottomSheetNestedScrollConnection(
  private val state: SKBottomSheetState,
  private val coroutineScope: CoroutineScope,
  private val onHide: () -> Unit
) : NestedScrollConnection {

  override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
    return if (source == NestedScrollSource.UserInput && available.y < 0f) {
      onScroll(available)
    } else {
      Offset.Zero
    }
  }

  override fun onPostScroll(
    consumed: Offset,
    available: Offset,
    source: NestedScrollSource
  ): Offset {
    return if (source == NestedScrollSource.UserInput && available.y > 0f) {
      onScroll(available)
    } else {
      Offset.Zero
    }
  }

  override suspend fun onPreFling(available: Velocity): Velocity {
    if (state.totalDragAmount != 0f) {
      coroutineScope.launch {
        val dismissed = state.onDragEnd()
        if (dismissed) onHide()
      }
      return available
    }
    return Velocity.Zero
  }

  private fun onScroll(available: Offset): Offset {
    val newOffset = state.totalDragAmount + available.y
    return if (newOffset >= 0f) {
      coroutineScope.launch { state.onDrag(available.y) }
      Offset(0f, available.y)
    } else {
      Offset.Zero
    }
  }
}
