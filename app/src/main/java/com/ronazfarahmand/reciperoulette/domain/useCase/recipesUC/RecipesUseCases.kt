package com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC

import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.apiUC.GetRecipeUC
import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.getRecipe.GetRecipeByIdUC
import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.upsertRecipe.UpsertRecipeUC

data class RecipesUseCases(
    val getRecipe: GetRecipeUC,
    val getRecipeById: GetRecipeByIdUC,
    val upsertRecipe: UpsertRecipeUC
)
