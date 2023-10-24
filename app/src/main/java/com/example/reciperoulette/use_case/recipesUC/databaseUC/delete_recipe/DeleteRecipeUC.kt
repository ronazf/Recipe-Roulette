package com.example.reciperoulette.use_case.recipesUC.databaseUC.delete_recipe

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