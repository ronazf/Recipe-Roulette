package com.example.reciperoulette.presentation.screens.recipeScreen.userActions

import com.example.reciperoulette.data.local.recipes.entities.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val editRecipe: Recipe? = null,
    val loading: Boolean = false,
    val info: Boolean = false,
    val isEditing: Boolean = false,
    val editStep: Int? = null,
    val addStep: Boolean = false,
    val showDialog: Boolean = false,
    val error: String = "",
    val success: String = ""
)
