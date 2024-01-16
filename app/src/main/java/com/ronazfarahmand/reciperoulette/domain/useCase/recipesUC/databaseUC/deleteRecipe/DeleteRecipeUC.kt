package com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.deleteRecipe

import com.ronazfarahmand.reciperoulette.data.local.recipes.entities.Recipe
import com.ronazfarahmand.reciperoulette.domain.repository.RecipeRepository
import javax.inject.Inject

class DeleteRecipeUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) {
        recipeRepository.deleteRecipe(recipe)
    }
}
