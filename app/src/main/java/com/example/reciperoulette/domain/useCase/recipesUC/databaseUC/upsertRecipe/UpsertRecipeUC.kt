package com.example.reciperoulette.domain.useCase.recipesUC.databaseUC.upsertRecipe

import com.example.reciperoulette.data.local.recipes.entities.Recipe
import com.example.reciperoulette.domain.repository.RecipeRepository
import javax.inject.Inject

class UpsertRecipeUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe): Long {
        return recipeRepository.upsertRecipe(recipe)
    }
}
