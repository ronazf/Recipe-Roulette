package com.example.reciperoulette.use_case.ingredientsUC.databaseUC.get_ingredients

import com.example.reciperoulette.activities.recipeGeneratorActivity.userActions.Filter
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