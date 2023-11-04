package com.example.reciperoulette.presentation.activities.mainActivity

import android.app.Activity
import android.os.Bundle
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.reciperoulette.R
import com.example.reciperoulette.presentation.activities.components.Loading
import com.example.reciperoulette.presentation.activities.navigation.NavBar
import com.example.reciperoulette.presentation.activities.screenRoutes.Screen
import com.example.reciperoulette.presentation.activities.screens.homeScreen.HomeScreen
import com.example.reciperoulette.presentation.activities.screens.ingredientScreen.IngredientSelectionScreen
import com.example.reciperoulette.presentation.activities.screens.libraryScreen.LibraryScreen
import com.example.reciperoulette.presentation.activities.screens.recipeScreen.RecipeScreen
import com.example.reciperoulette.presentation.ui.theme.RecipeRuletteTheme
import com.example.reciperoulette.domain.viewModel.IngredientViewModel
import com.example.reciperoulette.domain.viewModel.LibraryViewModel
import com.example.reciperoulette.domain.viewModel.RecipeViewModel
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
            var loading by remember { mutableStateOf(false) }
            var loadingResource by remember { mutableIntStateOf(R.drawable.grocery_shelf) }
            var loadingDescription by remember { mutableStateOf("") }

            RecipeRuletteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        containerColor = colorResource(id = R.color.transparent),
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
                                                route = Screen.RecipeGenerateScreen.route + "/$it"
                                            )
                                        },
                                        onIngredientEvent = ingredientViewModel::onIngredientEvent,
                                        onLoad = { resource, resourceDescription ->
                                            loading = true
                                            loadingResource = resource
                                            loadingDescription = resourceDescription
                                        },
                                        onResult = { loading = false }
                                    )
                                }
                                composable(
                                    route = Screen.RecipeGenerateScreen.route + "/{selected_ingredients}",
                                    arguments = listOf(
                                        navArgument("selected_ingredients") {}
                                    )
                                ) {
                                    val selectedIngredients = it.arguments
                                        ?.getString("selected_ingredients")
                                        ?.split(",") ?: emptyList()

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
                                        navigateBack = { navController.popBackStack() },
                                        onLoad = { resource, resourceDescription ->
                                            loading = true
                                            loadingResource = resource
                                            loadingDescription = resourceDescription
                                        },
                                        onResult = { loading = false },
                                        generated = true
                                    )
                                }
                            }
                            navigation(
                                startDestination = Screen.LibraryScreen.route,
                                route = Screen.LibraryRecipeScreen.route
                            ) {
                                composable(Screen.LibraryScreen.route) {
                                    val libraryViewModel: LibraryViewModel by viewModels()
                                    val state by libraryViewModel.state.collectAsState()

                                    LibraryScreen(
                                        state = state,
                                        navigateToRecipe = {
                                            navController.navigate(
                                                route = Screen.RecipeRenderScreen.route + "/$it"
                                            )
                                        },
                                        onLibraryEvent = libraryViewModel::onLibraryEvent
                                    )
                                }
                                composable(
                                    route = Screen.RecipeRenderScreen.route + "/{recipe_id}",
                                    arguments = listOf(
                                        navArgument("recipe_id") { type = NavType.LongType }
                                    )
                                ) {
                                    val recipeId = it.arguments
                                        ?.getLong("recipe_id")

                                    val factory = EntryPointAccessors.fromActivity(
                                        LocalContext.current as Activity,
                                        ViewModelFactoryProvider::class.java
                                    ).recipeViewModelFactory()

                                    val recipeViewModel: RecipeViewModel = viewModel(
                                        factory = RecipeViewModel.provideRecipeViewModelFactory(
                                            factory = factory,
                                            recipeId = recipeId
                                        )
                                    )

                                    val state by recipeViewModel.state.collectAsState()

                                    RecipeScreen(
                                        state = state,
                                        onRecipeEvent = recipeViewModel::onRecipeEvent,
                                        navigateBack = { navController.popBackStack() },
                                        onLoad = { resource, resourceDescription ->
                                            loading = true
                                            loadingResource = resource
                                            loadingDescription = resourceDescription
                                        },
                                        onResult = { loading = false }
                                    )
                                }
                            }
                        }
                    }
                }
                if (loading) {
                    Loading(resource = loadingResource, resourceDescription = loadingDescription)
                }
            }
        }
    }
}
