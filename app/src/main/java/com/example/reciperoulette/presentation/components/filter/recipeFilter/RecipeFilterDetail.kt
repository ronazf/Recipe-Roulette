package com.example.reciperoulette.presentation.components.filter.recipeFilter

import com.example.reciperoulette.R

enum class RecipeFilterDetail(val strName: String, val color: Int) {
    IS_FAVOURITE(strName = "Favourite", color = R.color.red),
    IS_GENERATED(strName = "Generated", color = R.color.dark_blue)
}
