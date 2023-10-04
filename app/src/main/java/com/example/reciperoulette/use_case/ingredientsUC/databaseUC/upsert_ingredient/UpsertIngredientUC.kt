package com.example.reciperoulette.use_case.ingredientsUC.databaseUC.upsert_ingredient

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