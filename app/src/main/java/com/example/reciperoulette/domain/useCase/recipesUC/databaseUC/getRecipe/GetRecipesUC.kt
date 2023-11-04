package com.example.reciperoulette.domain.useCase.recipesUC.databaseUC.getRecipe

import com.example.reciperoulette.data.local.recipes.entities.Recipe
import com.example.reciperoulette.domain.repository.RecipeRepository
import com.example.reciperoulette.presentation.screens.libraryScreen.userActions.RecipeFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    operator fun invoke(filter: RecipeFilter = RecipeFilter(), searchText: String = ""): Flow<List<Recipe>> {
        return recipeRepository.getRecipes(filter = filter, searchText = searchText)
    }
}
