package com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.databaseUC.deleteIngredient

import com.ronazfarahmand.reciperoulette.data.local.ingredients.entities.Ingredient
import com.ronazfarahmand.reciperoulette.domain.repository.IngredientsRepository
import javax.inject.Inject

class DeleteIngredientUC @Inject constructor(
    private val ingredientsRepository: IngredientsRepository
) {
    suspend operator fun invoke(ingredient: Ingredient) {
        return ingredientsRepository.deleteIngredient(ingredient)
    }
}
