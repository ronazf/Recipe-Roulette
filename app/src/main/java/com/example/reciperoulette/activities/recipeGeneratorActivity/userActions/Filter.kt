package com.example.reciperoulette.activities.recipeGeneratorActivity.userActions

data class Filter(
    val vegetarian: Boolean = false,
    val pescatarian: Boolean = false,
    val nutFree: Boolean = false,
    val dairyFree: Boolean = false
)
