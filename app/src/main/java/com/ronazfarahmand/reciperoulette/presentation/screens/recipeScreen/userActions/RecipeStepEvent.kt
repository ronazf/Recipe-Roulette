package com.ronazfarahmand.reciperoulette.presentation.screens.recipeScreen.userActions

sealed interface RecipeStepEvent {
    data class EditStepNumber(val step: Int) : RecipeStepEvent
    data class EditMinutes(val minutes: Int?) : RecipeStepEvent
    data class EditInstructions(val instructions: String) : RecipeStepEvent
}
