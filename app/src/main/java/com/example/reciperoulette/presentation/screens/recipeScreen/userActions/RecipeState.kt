package com.example.reciperoulette.presentation.screens.recipeScreen.userActions

import com.example.reciperoulette.data.local.recipes.entities.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val loading: Boolean = false,
    val info: Boolean = false,
    val error: String = "",
    val success: String = ""
)
