package com.example.reciperoulette.domain.repository

import com.example.reciperoulette.common.Resource
import com.example.reciperoulette.data.local.recipes.entities.Recipe
import com.example.reciperoulette.data.remote.api.response.Completion
import com.example.reciperoulette.presentation.activities.screens.libraryScreen.userActions.RecipeFilter
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun getRecipe(ingredients: List<String>): Resource<Completion>

    suspend fun getRecipeById(id: Long): Recipe

    suspend fun upsertRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)

    suspend fun setRecipeFavourite(id: Long)

    fun getRecipes(filter: RecipeFilter = RecipeFilter(), searchText: String = ""): Flow<List<Recipe>>
}
