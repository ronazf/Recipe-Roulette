package com.ronazfarahmand.reciperoulette.domain.repository

import com.ronazfarahmand.reciperoulette.common.Resource
import com.ronazfarahmand.reciperoulette.data.local.recipes.entities.Recipe
import com.ronazfarahmand.reciperoulette.data.remote.api.response.Completion
import com.ronazfarahmand.reciperoulette.presentation.screens.libraryScreen.userActions.RecipeFilter
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun getRecipe(ingredients: List<String>): Resource<Completion>

    suspend fun getRecipeById(id: Long): Recipe

    suspend fun upsertRecipe(recipe: Recipe): Long

    suspend fun deleteRecipe(recipe: Recipe)

    suspend fun setRecipeFavourite(id: Long)

    fun getRecipes(filter: RecipeFilter = RecipeFilter(), searchText: String = ""): Flow<List<Recipe>>
}
