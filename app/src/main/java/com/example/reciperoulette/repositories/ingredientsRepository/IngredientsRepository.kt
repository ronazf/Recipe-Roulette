package com.example.reciperoulette.repositories.ingredientsRepository

import com.example.reciperoulette.activities.recipeGeneratorActivity.userActions.Filter
import com.example.reciperoulette.api.response.Completion
import com.example.reciperoulette.api.response.Resource
import com.example.reciperoulette.database.ingredients.entities.Ingredient
import kotlinx.coroutines.flow.Flow

interface IngredientsRepository {

    fun getIngredients(filter: Filter): Flow<List<Ingredient>>

    suspend fun upsertIngredient(ingredient: Ingredient)

    suspend fun deleteIngredient(ingredient: Ingredient)

    suspend fun validateIngredient(ingredient: String): Resource<Completion>
}