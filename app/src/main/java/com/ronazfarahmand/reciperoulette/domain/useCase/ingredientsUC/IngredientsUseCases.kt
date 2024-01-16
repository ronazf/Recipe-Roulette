package com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC

import com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.apiUC.ValidateIngredientUC
import com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.databaseUC.deleteIngredient.DeleteIngredientUC
import com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.databaseUC.getIngredients.GetIngredientsUC
import com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.databaseUC.upsertIngredient.UpsertIngredientUC

data class IngredientsUseCases(
    val validateIngredient: ValidateIngredientUC,
    val deleteIngredient: DeleteIngredientUC,
    val getIngredients: GetIngredientsUC,
    val upsertIngredient: UpsertIngredientUC
)
