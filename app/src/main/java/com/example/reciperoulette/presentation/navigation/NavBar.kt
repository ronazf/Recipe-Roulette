package com.example.reciperoulette.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.reciperoulette.R
import com.example.reciperoulette.presentation.screenRoutes.Screen
import kotlin.reflect.full.isSubclassOf

@Composable
fun NavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val curRoute =
        navBackStackEntry?.destination?.parent?.route ?: navBackStackEntry?.destination?.route

    if (curRoute == null || curRoute == Screen.HomeScreen.route) return

    NavigationBar(
        containerColor = colorResource(id = R.color.navigation_bar),
        contentColor = colorResource(id = R.color.black)
    ) {
        NavBarItem::class.nestedClasses
            .filter { it.isSubclassOf(NavBarItem::class) }
            .map { it.objectInstance }
            .filterIsInstance<NavBarItem>()
            .forEach { navBarItem ->
                NavigationBarItem(
                    selected = curRoute == navBarItem.route,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(id = R.color.black),
                        unselectedIconColor = colorResource(id = R.color.dark_grey),
                        selectedTextColor = colorResource(id = R.color.black),
                        unselectedTextColor = colorResource(id = R.color.dark_grey),
                        indicatorColor = colorResource(id = R.color.transparent)
                    ),
                    onClick = {
                        navController.navigate(navBarItem.route)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = navBarItem.icon),
                            contentDescription = navBarItem.title
                        )
                    },
                    label = {
                        Text(
                            text = navBarItem.title
                        )
                    }
                )
            }
    }
}
