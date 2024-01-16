package com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.getRecipe

import com.ronazfarahmand.reciperoulette.data.local.recipes.entities.Recipe
import com.ronazfarahmand.reciperoulette.domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipeByIdUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(id: Long): Recipe {
        return recipeRepository.getRecipeById(id = id)
    }
}
