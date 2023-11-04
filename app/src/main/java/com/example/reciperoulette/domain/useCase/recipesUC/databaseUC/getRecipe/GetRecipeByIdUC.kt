package com.example.reciperoulette.domain.useCase.recipesUC.databaseUC.getRecipe

import com.example.reciperoulette.data.local.recipes.entities.Recipe
import com.example.reciperoulette.domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipeByIdUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(id: Long): Recipe {
        return recipeRepository.getRecipeById(id = id)
    }
}
