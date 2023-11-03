package com.example.reciperoulette.use_case.recipesUC

import com.example.reciperoulette.use_case.recipesUC.apiUC.GetRecipeUC
import com.example.reciperoulette.use_case.recipesUC.databaseUC.get_recipe.GetRecipeByIdUC
import com.example.reciperoulette.use_case.recipesUC.databaseUC.upsert_recipe.UpsertRecipeUC

data class RecipesUseCases(
    val getRecipe: GetRecipeUC,
    val getRecipeById: GetRecipeByIdUC,
    val upsertRecipe: UpsertRecipeUC
)
