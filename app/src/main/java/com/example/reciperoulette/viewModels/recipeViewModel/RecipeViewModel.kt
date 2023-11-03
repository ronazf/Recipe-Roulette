package com.example.reciperoulette.viewModels.recipeViewModel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reciperoulette.activities.screens.recipeScreen.userActions.RecipeEvent
import com.example.reciperoulette.activities.screens.recipeScreen.userActions.RecipeState
import com.example.reciperoulette.api.response.Resource
import com.example.reciperoulette.database.recipes.entities.Recipe
import com.example.reciperoulette.use_case.recipesUC.RecipesUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeViewModel @AssistedInject constructor(
    private val recipeUC: RecipesUseCases,
    @Assisted private val selectedIngredients: List<String>?,
    @Assisted private val recipeId: Long?
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(selectedIngredients: List<String>?, recipeId: Long?): RecipeViewModel
    }

    companion object {
        const val UNKNOWN_ERROR = "An unknown error has occurred."
        const val DUPLICATE_RECIPE = "This recipe is already in your saved recipe list."
        const val RECIPE_ADDED =
            "Recipe was successfully added to your recipe list."
        private const val SAFE_WAIT = 5000L

        fun provideRecipeViewModelFactory(
            factory: Factory,
            selectedIngredients: List<String>? = null,
            recipeId: Long? = null
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(selectedIngredients, recipeId) as T
                }
            }
        }
    }

    private val _state = MutableStateFlow(RecipeState())
    val state: StateFlow<RecipeState> = _state

    init {
        require(selectedIngredients != null || recipeId != null) {
            _state.update {
                it.copy(
                    error = UNKNOWN_ERROR
                )
            }
        }
        selectedIngredients?.let { getRecipe(selectedIngredients) }
        recipeId?.let {
            getRecipeById(recipeId)
        }
    }

    fun onRecipeEvent(event: RecipeEvent) {
        when (event) {
            is RecipeEvent.AddRecipeStep -> {
                val updatedRecipe = _state.value.recipe?.steps?.toMutableList()
                updatedRecipe?.add(
                    event.index,
                    event.recipeStep
                )
                _state.update {
                    it.copy(
                        recipe = updatedRecipe?.toList()?.let { steps ->
                            _state.value.recipe?.copy(
                                steps = steps
                            )
                        }
                    )
                }
            }

            RecipeEvent.ShowInfo -> {
                _state.update {
                    it.copy(
                        info = true
                    )
                }
            }

            RecipeEvent.DismissInfo -> {
                _state.update {
                    it.copy(
                        info = false
                    )
                }
            }

            RecipeEvent.ClearError -> {
                _state.update {
                    it.copy(
                        error = ""
                    )
                }
            }

            RecipeEvent.ClearSuccess -> {
                _state.update {
                    it.copy(
                        success = ""
                    )
                }
            }

            is RecipeEvent.EditRecipe -> {
                val updatedRecipe = _state.value.recipe?.steps?.toMutableList()
                updatedRecipe?.set(
                    event.index,
                    event.recipeStep
                )
                _state.update {
                    it.copy(
                        recipe = updatedRecipe?.toList()?.let { steps ->
                            _state.value.recipe?.copy(
                                steps = steps
                            )
                        }
                    )
                }
            }

            RecipeEvent.RegenerateRecipe -> {
                selectedIngredients?.let { getRecipe(selectedIngredients) }
            }

            RecipeEvent.SaveRecipe -> {
                _state.value.recipe?.let { upsertRecipe(it) }
            }
        }
    }

    private fun upsertRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                recipeUC.upsertRecipe(recipe)
                _state.update { recipeState ->
                    recipeState.copy(
                        success = RECIPE_ADDED
                    )
                }
            } catch (e: SQLiteConstraintException) {
                _state.update { recipeState ->
                    recipeState.copy(
                        error = DUPLICATE_RECIPE
                    )
                }
            }
        }
    }

    private fun getRecipeById(id: Long) {
        viewModelScope.launch {
            val recipe = recipeUC.getRecipeById(id)
            _state.update {
                it.copy(
                    recipe = recipe
                )
            }
        }
    }

    private fun getRecipe(selectedIngredients: List<String>) {
        viewModelScope.launch {
            recipeUC.getRecipe(selectedIngredients).collect {
                when (it) {
                    is Resource.Error -> {
                        _state.update { recipeState ->
                            recipeState.copy(
                                loading = false,
                                error = it.message?.ifEmpty { UNKNOWN_ERROR } ?: UNKNOWN_ERROR
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update { recipeState ->
                            recipeState.copy(
                                loading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update { recipeState ->
                            recipeState.copy(
                                loading = false,
                                recipe = it.data
                            )
                        }
                    }
                }
            }
        }
    }
}