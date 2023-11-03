package com.example.reciperoulette.use_case.recipesUC.databaseUC.get_recipe

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