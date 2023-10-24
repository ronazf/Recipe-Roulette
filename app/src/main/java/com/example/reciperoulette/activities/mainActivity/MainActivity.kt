package com.example.reciperoulette.activities.mainActivity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.reciperoulette.activities.navigation.NavBar
import com.example.reciperoulette.activities.screenRoutes.Screen
import com.example.reciperoulette.activities.screens.homeScreen.HomeScreen
import com.example.reciperoulette.activities.screens.ingredientScreen.IngredientSelectionScreen
import com.example.reciperoulette.activities.screens.libraryScreen.LibraryScreen
import com.example.reciperoulette.activities.screens.recipeScreen.RecipeScreen
import com.example.reciperoulette.viewModels.ingredientViewModel.IngredientViewModel
import com.example.reciperoulette.ui.theme.RecipeRuletteTheme
import com.example.reciperoulette.viewModels.recipeViewModel.RecipeViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun recipeViewModelFactory(): RecipeViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            RecipeRuletteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = { NavBar(navController = navController) }
                    ) { padding ->
                        NavHost(
                            modifier = Modifier.padding(padding),
                            navController = navController,
                            startDestination = Screen.HomeScreen.route
                        ) {
                            composable(Screen.HomeScreen.route) {
                                HomeScreen {
                                    navController.navigate(Screen.IngredientRecipeScreen.route)
                                }
                            }
                            navigation(
                                startDestination = Screen.IngredientSelectionScreen.route,
                                route = Screen.IngredientRecipeScreen.route
                            ) {
                                composable(Screen.IngredientSelectionScreen.route) {
                                    val ingredientViewModel: IngredientViewModel by viewModels()
                                    val state by ingredientViewModel.state.collectAsState()

                                    IngredientSelectionScreen(
                                        state = state,
                                        navigateToRecipe = {
                                            navController.navigate(
                                                route = Screen.RecipeScreen.route + "/$it"
                                            )
                                        },
                                        onIngredientEvent = ingredientViewModel::onIngredientEvent
                                    )
                                }
                                composable(
                                    route = Screen.RecipeScreen.route + "/{selected_ingredients}",
                                    arguments = listOf(
                                        navArgument("selected_ingredients") {}
                                    )
                                ) {
                                    val selectedIngredients = it.arguments
                                        ?.getString("selected_ingredients")
                                        ?.split(",") ?: emptyList()
                                    Log.d("success", selectedIngredients.toString())

                                    val factory = EntryPointAccessors.fromActivity(
                                        LocalContext.current as Activity,
                                        ViewModelFactoryProvider::class.java
                                    ).recipeViewModelFactory()

                                    val recipeViewModel: RecipeViewModel = viewModel(
                                        factory = RecipeViewModel.provideRecipeViewModelFactory(
                                            factory = factory,
                                            selectedIngredients = selectedIngredients
                                        )
                                    )

                                    val state by recipeViewModel.state.collectAsState()

                                    RecipeScreen(
                                        state = state,
                                        onRecipeEvent = recipeViewModel::onRecipeEvent,
                                        navigateBack = { navController.popBackStack() }
                                    )
                                }
                            }
                            composable(Screen.LibraryScreen.route) {
                                LibraryScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}