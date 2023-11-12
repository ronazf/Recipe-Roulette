package com.example.reciperoulette.presentation.screens.recipeScreen.userActions

import com.example.reciperoulette.data.local.recipes.RecipeIngredient

sealed interface RecipeInfoEvent {
    data object AddIngredient : RecipeInfoEvent
    data object ResetEditRecipe : RecipeInfoEvent
    data class RemoveIngredient(val id: Long) : RecipeInfoEvent
    data class EditIngredient(val ingredient: RecipeIngredient, val index: Int) : RecipeInfoEvent
    data class EditRecipeName(val recipeName: String) : RecipeInfoEvent
    data class EditRecipeServing(val serves: Int?) : RecipeInfoEvent
}
