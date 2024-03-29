package com.ronazfarahmand.reciperoulette.presentation.mainActivity

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
import com.ronazfarahmand.reciperoulette.R
import com.ronazfarahmand.reciperoulette.domain.viewModel.IngredientViewModel
import com.ronazfarahmand.reciperoulette.domain.viewModel.LibraryViewModel
import com.ronazfarahmand.reciperoulette.domain.viewModel.RecipeViewModel
import com.ronazfarahmand.reciperoulette.presentation.components.Loading
import com.ronazfarahmand.reciperoulette.presentation.navigation.NavBar
import com.ronazfarahmand.reciperoulette.presentation.screenRoutes.Screen
import com.ronazfarahmand.reciperoulette.presentation.screens.homeScreen.HomeScreen
import com.ronazfarahmand.reciperoulette.presentation.screens.ingredientScreen.IngredientSelectionScreen
import com.ronazfarahmand.reciperoulette.presentation.screens.libraryScreen.LibraryScreen
import com.ronazfarahmand.reciperoulette.presentation.screens.recipeScreen.RecipeScreen
import com.ronazfarahmand.reciperoulette.presentation.ui.theme.RecipeRuletteTheme
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
                                        onRecipeInfoEvent = recipeViewModel::onRecipeInfoEvent,
                                        onRecipeStepEvent = recipeViewModel::onRecipeStepEvent,
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
                                                route = Screen.RecipeRenderScreen.route +
                                                    (it?.let { "?recipe_id=$it" } ?: "")
                                            )
                                        },
                                        onLibraryEvent = libraryViewModel::onLibraryEvent
                                    )
                                }
                                composable(
                                    route = Screen.RecipeRenderScreen.route + "?recipe_id={recipe_id}",
                                    arguments = listOf(
                                        navArgument("recipe_id") {
                                            type = NavType.StringType
                                            nullable = true
                                        }
                                    )
                                ) {
                                    val recipeId = it.arguments
                                        ?.getString("recipe_id")?.toLong()

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
                                        onRecipeInfoEvent = recipeViewModel::onRecipeInfoEvent,
                                        onRecipeStepEvent = recipeViewModel::onRecipeStepEvent,
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
