package com.ronazfarahmand.reciperoulette.presentation.screens.recipeScreen.userActions

sealed interface RecipeEvent {
    data class DragRecipe(val beginIndex: Int, val endIndex: Int) : RecipeEvent
    data object SaveEditRecipeStep : RecipeEvent
    data object SaveEditRecipeInfo : RecipeEvent
    data object AddRecipeStep : RecipeEvent
    data class EditRecipeInfo(val isEditing: Boolean) : RecipeEvent
    data class EditRecipeStep(val recipeStep: Int, val isEditing: Boolean) : RecipeEvent
    data object DismissStep : RecipeEvent
    data object ShowInfo : RecipeEvent
    data object DismissInfo : RecipeEvent
    data object RegenerateRecipe : RecipeEvent
    data object SaveRecipe : RecipeEvent
    data object ClearError : RecipeEvent
    data object ClearSuccess : RecipeEvent
}
