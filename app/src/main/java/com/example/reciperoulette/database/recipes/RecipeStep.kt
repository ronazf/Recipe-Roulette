package com.example.reciperoulette.database.recipes

data class RecipeStep (
    val stepNumber: Int? = null,
    val instructions: String,
    val minutes: Int? = null
)