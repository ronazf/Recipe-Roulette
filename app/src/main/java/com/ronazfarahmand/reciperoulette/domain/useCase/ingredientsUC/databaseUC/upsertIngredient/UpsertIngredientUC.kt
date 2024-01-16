package com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.databaseUC.upsertIngredient

import com.ronazfarahmand.reciperoulette.data.local.ingredients.entities.Ingredient
import com.ronazfarahmand.reciperoulette.domain.repository.IngredientsRepository
import javax.inject.Inject

class UpsertIngredientUC @Inject constructor(
    private val ingredientsRepository: IngredientsRepository
) {
    suspend operator fun invoke(ingredient: Ingredient) {
        return ingredientsRepository.upsertIngredient(ingredient)
    }
}
