package com.example.reciperoulette.domain.useCase.ingredientsUC.databaseUC.getIngredients

import com.example.reciperoulette.data.local.ingredients.entities.Ingredient
import com.example.reciperoulette.domain.repository.IngredientsRepository
import com.example.reciperoulette.presentation.screens.ingredientScreen.userActions.Filter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIngredientsUC @Inject constructor(
    private val ingredientsRepository: IngredientsRepository
) {
    operator fun invoke(filter: Filter = Filter(), searchText: String = ""): Flow<List<Ingredient>> {
        return ingredientsRepository.getIngredients(filter, searchText)
    }
}
