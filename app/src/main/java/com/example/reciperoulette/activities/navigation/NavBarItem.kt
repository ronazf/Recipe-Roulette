package com.example.reciperoulette.activities.navigation

import com.example.reciperoulette.R
import com.example.reciperoulette.activities.screenRoutes.Screen

sealed class NavBarItem(val title: String, val icon: Int, val route: String) {
    data object Library: NavBarItem("Library", R.drawable.library, Screen.LibraryRecipeScreen.route)
    data object AIGenerator: NavBarItem("AI Generator", R.drawable.generator, Screen.IngredientRecipeScreen.route)
}
