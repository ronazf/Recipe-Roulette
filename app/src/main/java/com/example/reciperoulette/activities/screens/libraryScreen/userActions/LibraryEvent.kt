package com.example.reciperoulette.activities.screens.libraryScreen.userActions

import com.example.reciperoulette.database.recipes.entities.Recipe

sealed interface LibraryEvent {
    data class FilterRecipes(val filter: RecipeFilter) : LibraryEvent
    data class SearchRecipe(val searchText: String = "") : LibraryEvent
    data class DeleteRecipe(val recipe: Recipe) : LibraryEvent
    data class SelectRecipe(val id: Long) : LibraryEvent
    data class SetFavourite(val id: Long) : LibraryEvent
    data object ClearSearch : LibraryEvent
    data object ClearError : LibraryEvent
    data object ClearSuccess : LibraryEvent
}
