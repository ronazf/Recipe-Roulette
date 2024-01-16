package com.ronazfarahmand.reciperoulette.presentation.components.dragDropList

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState

val LazyListItemInfo.offsetEnd: Int
    get() = this.offset + this.size

fun LazyListState.getVisibleItemInfoFor(index: Int): LazyListItemInfo? {
    return this.layoutInfo.visibleItemsInfo.getOrNull(
        index - this.layoutInfo.visibleItemsInfo.first().index
    )
}
