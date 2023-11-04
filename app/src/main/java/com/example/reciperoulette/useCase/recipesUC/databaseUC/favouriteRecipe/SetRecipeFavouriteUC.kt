package com.example.reciperoulette.useCase.recipesUC.databaseUC.favouriteRecipe

import com.example.reciperoulette.repositories.recipeRepository.RecipeRepository
import javax.inject.Inject

class SetRecipeFavouriteUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(id: Long) {
        recipeRepository.setRecipeFavourite(id)
    }
}
