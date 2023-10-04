package com.example.reciperoulette.activities.recipeGeneratorActivity.userActions

import com.example.reciperoulette.database.ingredients.CategoryDetail
import com.example.reciperoulette.database.ingredients.entities.Ingredient
import java.util.EnumMap

data class IngredientState(
    val ingredients: Map<CategoryDetail, List<Ingredient>> = EnumMap(CategoryDetail::class.java),
    val filter: Filter = Filter(),
    val loading: Boolean = false,
    val verifyingIngredient: Boolean = false,
    val searchName: String = "",
    val error: String = ""
)
