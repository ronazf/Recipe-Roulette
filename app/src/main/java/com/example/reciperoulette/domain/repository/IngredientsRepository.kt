package com.example.reciperoulette.domain.repository

import com.example.reciperoulette.presentation.activities.screens.ingredientScreen.userActions.Filter
import com.example.reciperoulette.data.remote.api.response.Completion
import com.example.reciperoulette.common.Resource
import com.example.reciperoulette.data.local.ingredients.entities.Ingredient
import kotlinx.coroutines.flow.Flow

interface IngredientsRepository {

    fun getIngredients(filter: Filter = Filter(), searchText: String = ""): Flow<List<Ingredient>>

    suspend fun upsertIngredient(ingredient: Ingredient)

    suspend fun deleteIngredient(ingredient: Ingredient)

    suspend fun validateIngredient(ingredient: String): Resource<Completion>
}
