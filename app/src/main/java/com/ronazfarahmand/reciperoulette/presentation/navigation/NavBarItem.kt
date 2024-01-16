package com.ronazfarahmand.reciperoulette.presentation.navigation

import com.ronazfarahmand.reciperoulette.R
import com.ronazfarahmand.reciperoulette.presentation.screenRoutes.Screen

sealed class NavBarItem(val title: String, val icon: Int, val route: String) {
    data object Library : NavBarItem("Library", R.drawable.library, Screen.LibraryRecipeScreen.route)
    data object AIGenerator : NavBarItem("AI Generator", R.drawable.generator, Screen.IngredientRecipeScreen.route)
}
