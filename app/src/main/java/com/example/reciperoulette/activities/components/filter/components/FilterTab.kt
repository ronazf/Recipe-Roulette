package com.example.reciperoulette.activities.components.filter.components

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
import com.example.reciperoulette.activities.components.filter.FilterDetail
import com.example.reciperoulette.activities.recipeGeneratorActivity.userActions.Filter
import com.example.reciperoulette.activities.recipeGeneratorActivity.userActions.IngredientEvent

@Composable
fun FilterTab(
    modifier: Modifier,
    filter: Filter,
    onIngredientEvent: (IngredientEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Row (
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        enumValues<FilterDetail>().forEach {
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
                        filterIngredients(
                            filter = filter,
                            checked = checked,
                            filterDetail = it,
                            onIngredientEvent = onIngredientEvent
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
    filter: Filter,
    filterDetail: FilterDetail
): Boolean {
    return when (filterDetail) {
        FilterDetail.IS_VEGETARIAN -> filter.vegetarian
        FilterDetail.IS_PESCATARIAN -> filter.pescatarian
        FilterDetail.IS_NUT_FREE -> filter.nutFree
        FilterDetail.IS_DAIRY_FREE -> filter.dairyFree
    }
}

fun filterIngredients(
    filter: Filter,
    checked: Boolean,
    filterDetail: FilterDetail,
    onIngredientEvent: (IngredientEvent) -> Unit
) {
    when (filterDetail) {
        FilterDetail.IS_VEGETARIAN -> {
            onIngredientEvent(
                IngredientEvent.FilterIngredients(
                    filter = filter.copy(
                        vegetarian = checked
                    )
                )
            )
        }

        FilterDetail.IS_PESCATARIAN -> {
            onIngredientEvent(
                IngredientEvent.FilterIngredients(
                    filter = filter.copy(
                        pescatarian = checked
                    )
                )
            )
        }

        FilterDetail.IS_NUT_FREE -> {
            onIngredientEvent(
                IngredientEvent.FilterIngredients(
                    filter = filter.copy(
                        nutFree = checked
                    )
                )
            )
        }

        FilterDetail.IS_DAIRY_FREE -> {
            onIngredientEvent(
                IngredientEvent.FilterIngredients(
                    filter = filter.copy(
                        dairyFree = checked
                    )
                )
            )
        }
    }
}