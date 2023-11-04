package com.example.reciperoulette.domain.useCase.recipesUC

import com.example.reciperoulette.domain.useCase.recipesUC.apiUC.GetRecipeUC
import com.example.reciperoulette.domain.useCase.recipesUC.databaseUC.deleteRecipe.DeleteRecipeUC
import com.example.reciperoulette.domain.useCase.recipesUC.databaseUC.favouriteRecipe.SetRecipeFavouriteUC
import com.example.reciperoulette.domain.useCase.recipesUC.databaseUC.getRecipe.GetRecipesUC

data class RecipeLibraryUseCases(
    val getRecipes: GetRecipesUC,
    val getRecipe: GetRecipeUC,
    val setRecipeFavourite: SetRecipeFavouriteUC,
    val deleteRecipe: DeleteRecipeUC
)
