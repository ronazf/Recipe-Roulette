package com.example.reciperoulette.presentation.screens.recipeScreen.userActions

sealed interface RecipeInfoEvent {
    data object AddIngredient : RecipeInfoEvent
    data class RemoveIngredient(val id: Long) : RecipeInfoEvent
    data class EditIngredient(val id: Long, val ingredient: String) : RecipeInfoEvent
    data class EditRecipeName(val recipeName: String) : RecipeInfoEvent
    data class EditRecipeServing(val serves: Int?) : RecipeInfoEvent
}
