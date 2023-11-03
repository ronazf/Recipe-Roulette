package com.example.reciperoulette.use_case.recipesUC

import com.example.reciperoulette.use_case.recipesUC.apiUC.GetRecipeUC
import com.example.reciperoulette.use_case.recipesUC.databaseUC.delete_recipe.DeleteRecipeUC
import com.example.reciperoulette.use_case.recipesUC.databaseUC.favourite_recipe.SetRecipeFavouriteUC
import com.example.reciperoulette.use_case.recipesUC.databaseUC.get_recipe.GetRecipesUC

data class RecipeLibraryUseCases(
    val getRecipes: GetRecipesUC,
    val getRecipe: GetRecipeUC,
    val setRecipeFavourite: SetRecipeFavouriteUC,
    val deleteRecipe: DeleteRecipeUC
)
