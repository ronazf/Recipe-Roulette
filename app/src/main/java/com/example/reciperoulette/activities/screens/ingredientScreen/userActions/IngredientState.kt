package com.example.reciperoulette.activities.screens.ingredientScreen.userActions

import com.example.reciperoulette.database.ingredients.details.CategoryDetail
import com.example.reciperoulette.database.ingredients.entities.Ingredient
import java.util.EnumMap

data class IngredientState(
    val ingredients: List<Ingredient> = emptyList(),
    val mappedIngredients: Map<CategoryDetail, List<Ingredient>> = EnumMap(CategoryDetail::class.java),
    val selectedIngredients: List<String> = emptyList(),
    val filter: Filter = Filter(),
    val searchText: String = "",
    val loading: Boolean = false,
    val verifyingIngredient: Boolean = false,
    val ingredientName: String = "",
    val error: String = "",
    val success: String = ""
)
