package com.example.reciperoulette.use_case.recipesUC.databaseUC.get_recipe

import com.example.reciperoulette.database.recipes.entities.Recipe
import com.example.reciperoulette.repositories.recipeRepository.RecipeRepository
import javax.inject.Inject

class GetRecipeByIdUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(id: Long): Recipe {
        return recipeRepository.getRecipeById(id = id)
    }
}