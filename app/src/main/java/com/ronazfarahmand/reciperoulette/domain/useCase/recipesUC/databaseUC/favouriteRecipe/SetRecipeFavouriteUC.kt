package com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.favouriteRecipe

import com.ronazfarahmand.reciperoulette.domain.repository.RecipeRepository
import javax.inject.Inject

class SetRecipeFavouriteUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(id: Long) {
        recipeRepository.setRecipeFavourite(id)
    }
}
