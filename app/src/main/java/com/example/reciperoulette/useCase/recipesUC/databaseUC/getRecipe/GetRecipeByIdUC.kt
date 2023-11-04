package com.example.reciperoulette.useCase.recipesUC.databaseUC.getRecipe

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
