package com.example.reciperoulette.domain.useCase.recipesUC.databaseUC.deleteRecipe

import com.example.reciperoulette.data.local.recipes.entities.Recipe
import com.example.reciperoulette.domain.repository.RecipeRepository
import javax.inject.Inject

class DeleteRecipeUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) {
        recipeRepository.deleteRecipe(recipe)
    }
}
