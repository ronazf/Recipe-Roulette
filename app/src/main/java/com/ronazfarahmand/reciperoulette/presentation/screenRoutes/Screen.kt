package com.ronazfarahmand.reciperoulette.presentation.screenRoutes

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home_screen")
    data object IngredientRecipeScreen : Screen("ingredient_recipe")
    data object IngredientSelectionScreen : Screen("ingredient_selection_screen")
    data object LibraryRecipeScreen : Screen("library_recipe")
    data object RecipeGenerateScreen : Screen("recipe_generate_screen")
    data object RecipeRenderScreen : Screen("recipe_render_screen")
    data object LibraryScreen : Screen("library_screen")
}
