package com.example.reciperoulette.use_case.ingredientsUC.databaseUC.delete_ingredient

import com.example.reciperoulette.database.ingredients.entities.Ingredient
import com.example.reciperoulette.repositories.ingredientsRepository.IngredientsRepository
import javax.inject.Inject

class DeleteIngredientUC @Inject constructor(
    private val ingredientsRepository: IngredientsRepository
) {
    suspend operator fun invoke(ingredient: Ingredient) {
        return ingredientsRepository.deleteIngredient(ingredient)
    }
}