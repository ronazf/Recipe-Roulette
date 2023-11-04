package com.example.reciperoulette.database.recipes

data class RecipeStep(
    val instructions: String,
    val minutes: Int? = null
)
