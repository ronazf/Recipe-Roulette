package com.example.reciperoulette.use_case.recipesUC.databaseUC.favourite_recipe

import com.example.reciperoulette.repositories.recipeRepository.RecipeRepository
import javax.inject.Inject

class SetRecipeFavouriteUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(id: Long) {
        recipeRepository.setRecipeFavourite(id)
    }
}