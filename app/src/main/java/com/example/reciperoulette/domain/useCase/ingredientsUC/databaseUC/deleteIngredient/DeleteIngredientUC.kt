package com.example.reciperoulette.domain.useCase.ingredientsUC.databaseUC.deleteIngredient

import com.example.reciperoulette.data.local.ingredients.entities.Ingredient
import com.example.reciperoulette.domain.repository.IngredientsRepository
import javax.inject.Inject

class DeleteIngredientUC @Inject constructor(
    private val ingredientsRepository: IngredientsRepository
) {
    suspend operator fun invoke(ingredient: Ingredient) {
        return ingredientsRepository.deleteIngredient(ingredient)
    }
}
