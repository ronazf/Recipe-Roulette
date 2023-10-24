package com.example.reciperoulette.activities.screenRoutes

sealed class Screen(val route: String) {
    data object HomeScreen: Screen("home_screen")
    data object IngredientRecipeScreen: Screen("ingredient_recipe")
    data object IngredientSelectionScreen: Screen("ingredient_selection_screen")
    data object RecipeScreen: Screen("recipe_screen")
    data object LibraryScreen: Screen("library_screen")
}