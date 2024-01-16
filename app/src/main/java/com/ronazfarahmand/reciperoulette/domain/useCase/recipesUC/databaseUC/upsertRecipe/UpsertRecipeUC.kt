package com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.upsertRecipe

import com.ronazfarahmand.reciperoulette.data.local.recipes.entities.Recipe
import com.ronazfarahmand.reciperoulette.domain.repository.RecipeRepository
import javax.inject.Inject

class UpsertRecipeUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe): Long {
        return recipeRepository.upsertRecipe(recipe)
    }
}
