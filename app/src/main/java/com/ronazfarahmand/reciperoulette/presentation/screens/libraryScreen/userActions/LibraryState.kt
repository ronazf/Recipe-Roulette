package com.ronazfarahmand.reciperoulette.presentation.screens.libraryScreen.userActions

import com.ronazfarahmand.reciperoulette.data.local.recipes.entities.Recipe

data class LibraryState(
    val recipes: List<Recipe> = emptyList(),
    val filter: RecipeFilter = RecipeFilter(),
    val searchText: String = "",
    val error: String = "",
    val success: String = ""
)
