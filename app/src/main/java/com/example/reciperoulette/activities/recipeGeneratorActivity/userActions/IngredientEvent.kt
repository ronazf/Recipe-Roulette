package com.example.reciperoulette.activities.recipeGeneratorActivity.userActions

import com.example.reciperoulette.database.ingredients.entities.Ingredient

sealed interface IngredientEvent {
    data class FilterIngredients(val filter: Filter = Filter()): IngredientEvent
    data class AddIngredient(val name: String): IngredientEvent
    data class RemoveIngredient(val ingredientName: String): IngredientEvent
    data class SelectIngredient(val name: String): IngredientEvent
    data class RemoveSelectedIngredient(val name: String): IngredientEvent
    data class SearchIngredient(val searchText: String = ""): IngredientEvent
    data object ClearSearch: IngredientEvent
    data object ClearError: IngredientEvent
    data object ClearSuccess: IngredientEvent
}