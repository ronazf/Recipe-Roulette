package com.example.reciperoulette.repositories.recipeRepository

import com.example.reciperoulette.api.response.Completion
import com.example.reciperoulette.api.response.Resource

interface RecipeRepository {

    suspend fun getRecipe(ingredients: List<String>): Resource<Completion>
}