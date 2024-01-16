package com.ronazfarahmand.reciperoulette.domain.repository

import com.ronazfarahmand.reciperoulette.common.Resource
import com.ronazfarahmand.reciperoulette.data.local.ingredients.entities.Ingredient
import com.ronazfarahmand.reciperoulette.data.remote.api.response.Completion
import com.ronazfarahmand.reciperoulette.presentation.screens.ingredientScreen.userActions.Filter
import kotlinx.coroutines.flow.Flow

interface IngredientsRepository {

    fun getIngredients(filter: Filter = Filter(), searchText: String = ""): Flow<List<Ingredient>>

    suspend fun upsertIngredient(ingredient: Ingredient)

    suspend fun deleteIngredient(ingredient: Ingredient)

    suspend fun validateIngredient(ingredient: String): Resource<Completion>
}
