package com.ronazfarahmand.reciperoulette.presentation.components.dragDropList

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DragDropListState(
    private val lazyListState: LazyListState,
    private val scope: CoroutineScope,
    private val onDrop: (beginIndex: Int, endIndex: Int) -> Unit
) {
    val elementDisplacement: Float?
        get() = indexOfDragged
            ?.let { lazyListState.getVisibleItemInfoFor(it) }
            ?.let { item ->
                (draggedElement?.offset ?: 0f).toFloat() + draggedDistance - item.offset
            }

    var indexOfDragged by mutableStateOf<Int?>(null)
        private set

    private val initialOffset: Pair<Int, Int>?
        get() = draggedElement?.let { Pair(it.offset, it.offsetEnd) }

    private var draggedDistance by mutableFloatStateOf(0F)
    private var draggedElement by mutableStateOf<LazyListItemInfo?>(null)
    private var scrollJob by mutableStateOf<Job?>(null)

    internal fun onDragStart(offset: Offset) {
        lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
            offset.y.toInt() in item.offset..item.offsetEnd
        }?.also { lazyListItem ->
            draggedElement = lazyListItem
            indexOfDragged = lazyListItem.index
        }
    }

    internal fun onDrag(offset: Offset) {
        draggedDistance += offset.y

        initialOffset?.let { (topOffset, bottomOffset) ->
            val startOffset = topOffset + draggedDistance
            val endOffset = bottomOffset + draggedDistance
            val middleOffset = (startOffset + endOffset) / 2F

            val targetItem = lazyListState.layoutInfo.visibleItemsInfo.find { item ->
                middleOffset.toInt() in item.offset..item.offsetEnd
            }

            if (targetItem != null) {
                indexOfDragged?.let { onDrop(it, targetItem.index) }
                indexOfDragged = targetItem.index
            }

            if (scrollJob?.isActive == true) {
                return
            }

            overScroll()
                .takeIf { it != 0F }
                ?.let {
                    scrollJob =
                        scope.launch {
                            lazyListState.scrollBy(it)
                        }
                } ?: run { scrollJob?.cancel() }
        }
    }

    internal fun onDragCancel() {
        draggedDistance = 0F
        indexOfDragged = null
        draggedElement = null
        scrollJob?.cancel()
    }

    private fun overScroll(): Float {
        return draggedElement?.let {
            val start = it.offset + draggedDistance
            val end = it.offsetEnd + draggedDistance

            return@let when {
                draggedDistance < 0 -> (start - lazyListState.layoutInfo.viewportStartOffset)
                    .takeIf { diff ->
                        diff < 0
                    }

                draggedDistance > 0 -> (end - lazyListState.layoutInfo.viewportEndOffset)
                    .takeIf { diff ->
                        diff > 0
                    }

                else -> 0F
            }
        } ?: 0F
    }
}

@Composable
fun rememberDraggableListState(
    lazyListState: LazyListState = rememberLazyListState(),
    scope: CoroutineScope,
    onDrop: (beginIndex: Int, endIndex: Int) -> Unit
): DragDropListState {
    return remember {
        DragDropListState(
            lazyListState = lazyListState,
            onDrop = onDrop,
            scope = scope
        )
    }
}
