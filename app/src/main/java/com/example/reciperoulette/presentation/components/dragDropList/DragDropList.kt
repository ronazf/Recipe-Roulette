package com.example.reciperoulette.presentation.components.dragDropList

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun <E> DragDropList(
    modifier: Modifier,
    horizontalAlignment: Alignment.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    contentPadding: PaddingValues,
    items: List<E>,
    onDrop: (begin: Int, end: Int) -> Unit,
    listContent: LazyListScope.() -> Unit,
    content: @Composable LazyItemScope.(index: Int, item: E, dragging: Boolean) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val dragDropListState = rememberDraggableListState(
        onDrop = onDrop,
        scope = scope,
        lazyListState = listState
    )

    LazyColumn(
        modifier = modifier.pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = { dragAmount ->
                    dragDropListState.onDragStart(dragAmount)
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    dragDropListState.onDrag(dragAmount)
                },
                onDragEnd = {
                    dragDropListState.onDragCancel()
                },
                onDragCancel = {
                    dragDropListState.onDragCancel()
                }
            )
        },
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
        contentPadding = contentPadding,
        state = listState
    ) {
        itemsIndexed(items) { index, item ->
            DragDropItem(
                index = index,
                dragDropState = dragDropListState
            ) { dragging ->
                content(index, item, dragging)
            }
        }
        listContent()
    }
}
