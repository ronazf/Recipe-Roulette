package com.example.reciperoulette.repositories.recipeRepository

import com.example.reciperoulette.activities.screens.libraryScreen.userActions.RecipeFilter
import com.example.reciperoulette.api.response.Completion
import com.example.reciperoulette.api.response.Resource
import com.example.reciperoulette.database.recipes.entities.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun getRecipe(ingredients: List<String>): Resource<Completion>

    suspend fun getRecipeById(id: Long): Recipe

    suspend fun upsertRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)

    suspend fun setRecipeFavourite(id: Long)

    fun getRecipes(filter: RecipeFilter = RecipeFilter(), searchText: String = ""): Flow<List<Recipe>>
}
