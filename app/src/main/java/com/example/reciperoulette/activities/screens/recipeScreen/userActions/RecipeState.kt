package com.example.reciperoulette.activities.screens.recipeScreen.userActions

import com.example.reciperoulette.database.recipes.entities.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val loading: Boolean = false,
    val info: Boolean = false,
    val error: String = "",
    val success: String = ""
)
