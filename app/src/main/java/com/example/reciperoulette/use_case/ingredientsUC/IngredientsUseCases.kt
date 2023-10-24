package com.example.reciperoulette.use_case.ingredientsUC

import com.example.reciperoulette.use_case.ingredientsUC.apiUC.ValidateIngredientUC
import com.example.reciperoulette.use_case.ingredientsUC.databaseUC.delete_ingredient.DeleteIngredientUC
import com.example.reciperoulette.use_case.ingredientsUC.databaseUC.get_ingredients.GetIngredientsUC
import com.example.reciperoulette.use_case.ingredientsUC.databaseUC.upsert_ingredient.UpsertIngredientUC

data class IngredientsUseCases(
    val validateIngredient: ValidateIngredientUC,
    val deleteIngredient: DeleteIngredientUC,
    val getIngredients: GetIngredientsUC,
    val upsertIngredient: UpsertIngredientUC
)
