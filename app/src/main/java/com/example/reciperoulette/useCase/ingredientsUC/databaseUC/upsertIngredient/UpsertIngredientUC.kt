package com.example.reciperoulette.useCase.ingredientsUC.databaseUC.upsertIngredient

import com.example.reciperoulette.database.ingredients.entities.Ingredient
import com.example.reciperoulette.repositories.ingredientsRepository.IngredientsRepository
import javax.inject.Inject

class UpsertIngredientUC @Inject constructor(
    private val ingredientsRepository: IngredientsRepository
) {
    suspend operator fun invoke(ingredient: Ingredient) {
        return ingredientsRepository.upsertIngredient(ingredient)
    }
}
