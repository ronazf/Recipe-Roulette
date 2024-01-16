package com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.databaseUC.getRecipe

import com.ronazfarahmand.reciperoulette.data.local.recipes.entities.Recipe
import com.ronazfarahmand.reciperoulette.domain.repository.RecipeRepository
import com.ronazfarahmand.reciperoulette.presentation.screens.libraryScreen.userActions.RecipeFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    operator fun invoke(filter: RecipeFilter = RecipeFilter(), searchText: String = ""): Flow<List<Recipe>> {
        return recipeRepository.getRecipes(filter = filter, searchText = searchText)
    }
}
