package com.example.reciperoulette.useCase.recipesUC.databaseUC.getRecipe

import com.example.reciperoulette.activities.screens.libraryScreen.userActions.RecipeFilter
import com.example.reciperoulette.database.recipes.entities.Recipe
import com.example.reciperoulette.repositories.recipeRepository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUC @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    operator fun invoke(filter: RecipeFilter = RecipeFilter(), searchText: String = ""): Flow<List<Recipe>> {
        return recipeRepository.getRecipes(filter = filter, searchText = searchText)
    }
}
