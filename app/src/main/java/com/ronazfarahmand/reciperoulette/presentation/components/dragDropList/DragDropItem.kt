package com.ronazfarahmand.reciperoulette.presentation.components.dragDropList

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.DragDropItem(
    modifier: Modifier = Modifier,
    index: Int,
    dragDropState: DragDropListState,
    content: @Composable ColumnScope.(isDragging: Boolean) -> Unit
) {
    val springSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessVeryLow
    )
    val translation = animateFloatAsState(
        animationSpec = springSpec,
        targetValue = dragDropState.elementDisplacement ?: 0F,
        label = ""
    )
    val dragging = (index == dragDropState.indexOfDragged)
    val draggingModifier = if (dragging) {
        modifier
            .zIndex(1F)
            .graphicsLayer {
                translationY = translation.value
            }
    } else {
        Modifier.animateItemPlacement()
    }
    Column(modifier = draggingModifier) {
        content(dragging)
    }
}
