package com.example.reciperoulette.use_case.recipesUC

import com.example.reciperoulette.use_case.recipesUC.apiUC.GetRecipeUC
import com.example.reciperoulette.use_case.recipesUC.databaseUC.delete_recipe.DeleteRecipeUC
import com.example.reciperoulette.use_case.recipesUC.databaseUC.get_recipes.GetRecipesUC
import com.example.reciperoulette.use_case.recipesUC.databaseUC.upsert_recipe.UpsertRecipeUC

data class RecipesUseCases(
    val getRecipes: GetRecipesUC,
    val getRecipe: GetRecipeUC,
    val deleteRecipe: DeleteRecipeUC,
    val upsertRecipe: UpsertRecipeUC
)
