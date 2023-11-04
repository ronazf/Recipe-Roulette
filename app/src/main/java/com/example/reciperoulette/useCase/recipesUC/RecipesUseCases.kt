package com.example.reciperoulette.useCase.recipesUC

import com.example.reciperoulette.useCase.recipesUC.apiUC.GetRecipeUC
import com.example.reciperoulette.useCase.recipesUC.databaseUC.getRecipe.GetRecipeByIdUC
import com.example.reciperoulette.useCase.recipesUC.databaseUC.upsertRecipe.UpsertRecipeUC

data class RecipesUseCases(
    val getRecipe: GetRecipeUC,
    val getRecipeById: GetRecipeByIdUC,
    val upsertRecipe: UpsertRecipeUC
)
