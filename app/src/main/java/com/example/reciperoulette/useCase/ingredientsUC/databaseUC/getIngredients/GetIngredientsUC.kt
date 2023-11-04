package com.example.reciperoulette.useCase.ingredientsUC.databaseUC.getIngredients

import com.example.reciperoulette.activities.screens.ingredientScreen.userActions.Filter
import com.example.reciperoulette.database.ingredients.entities.Ingredient
import com.example.reciperoulette.repositories.ingredientsRepository.IngredientsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIngredientsUC @Inject constructor(
    private val ingredientsRepository: IngredientsRepository
) {
    operator fun invoke(filter: Filter = Filter(), searchText: String = ""): Flow<List<Ingredient>> {
        return ingredientsRepository.getIngredients(filter, searchText)
    }
}
