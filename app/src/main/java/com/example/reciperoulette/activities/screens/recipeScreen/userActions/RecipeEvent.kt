package com.example.reciperoulette.activities.screens.recipeScreen.userActions

import com.example.reciperoulette.database.recipes.RecipeStep

sealed interface RecipeEvent {
    data class EditRecipe(val recipeStep: RecipeStep, val index: Int): RecipeEvent
    data class AddRecipeStep(val recipeStep: RecipeStep, val index: Int): RecipeEvent
    data object ShowInfo: RecipeEvent
    data object DismissInfo: RecipeEvent
    data object RegenerateRecipe: RecipeEvent
    data object SaveRecipe: RecipeEvent
    data object ClearError: RecipeEvent
    data object ClearSuccess: RecipeEvent
}