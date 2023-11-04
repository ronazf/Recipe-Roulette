package com.example.reciperoulette.useCase.recipesUC.databaseUC.deleteRecipe

import com.example.reciperoulette.database.recipes.entities.Recipe
import com.example.reciperoulette.repositories.recipeRepository.RecipeRepository
import javax.inject.Inject

class DeleteRecipeUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) {
        recipeRepository.deleteRecipe(recipe)
    }
}
