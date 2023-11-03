package com.example.reciperoulette.activities.components.filter.recipeFilter.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.reciperoulette.activities.GeneralConstants
import com.example.reciperoulette.activities.components.filter.recipeFilter.RecipeFilterDetail
import com.example.reciperoulette.activities.screens.libraryScreen.userActions.LibraryEvent
import com.example.reciperoulette.activities.screens.libraryScreen.userActions.RecipeFilter

@Composable
fun RecipeFilterTab(
    modifier: Modifier,
    filter: RecipeFilter,
    onLibraryEvent: (LibraryEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Row (
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        enumValues<RecipeFilterDetail>().forEach {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                var checked by remember { mutableStateOf(false) }
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = GeneralConstants.IMAGE_TEXT_PADDING),
                    text = it.strName,
                    fontSize = GeneralConstants.TEXT_FONT_SIZE,
                    fontFamily = GeneralConstants.FONT_FAMILY
                )
                Checkbox(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = GeneralConstants.IMAGE_TEXT_PADDING),
                    checked = getFilterState(filter, it),
                    onCheckedChange = { bool ->
                        checked = bool
                        filterRecipes(
                            filter = filter,
                            checked = checked,
                            filterDetail = it,
                            onLibraryEvent = onLibraryEvent
                        )
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorResource(id = it.color),
                        uncheckedColor = colorResource(id = it.color)
                    )
                )
            }
        }
    }
}

fun getFilterState(
    filter: RecipeFilter,
    filterDetail: RecipeFilterDetail
): Boolean {
    return when (filterDetail) {
        RecipeFilterDetail.IS_FAVOURITE -> filter.favourite
        RecipeFilterDetail.IS_GENERATED -> filter.generated
    }
}

fun filterRecipes(
    filter: RecipeFilter,
    checked: Boolean,
    filterDetail: RecipeFilterDetail,
    onLibraryEvent: (LibraryEvent) -> Unit
) {
    when (filterDetail) {
        RecipeFilterDetail.IS_FAVOURITE -> {
            onLibraryEvent(
                LibraryEvent.FilterRecipes(
                    filter = filter.copy(
                        favourite = checked
                    )
                )
            )
        }

        RecipeFilterDetail.IS_GENERATED -> {
            onLibraryEvent(
                LibraryEvent.FilterRecipes(
                    filter = filter.copy(
                        generated = checked
                    )
                )
            )
        }
    }
}