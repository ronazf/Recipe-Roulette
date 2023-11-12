package com.example.reciperoulette.domain.viewModel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reciperoulette.common.Resource
import com.example.reciperoulette.data.local.recipes.RecipeIngredient
import com.example.reciperoulette.data.local.recipes.entities.Recipe
import com.example.reciperoulette.domain.useCase.recipesUC.RecipesUseCases
import com.example.reciperoulette.presentation.screens.recipeScreen.userActions.RecipeEvent
import com.example.reciperoulette.presentation.screens.recipeScreen.userActions.RecipeInfoEvent
import com.example.reciperoulette.presentation.screens.recipeScreen.userActions.RecipeState
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
        const val RECIPE_UPDATED =
            "Recipe was successfully updated."

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
            is RecipeEvent.DragRecipe -> {
                if (event.beginIndex < 0 ||
                    event.endIndex < 0 ||
                    event.beginIndex >= (_state.value.recipe?.steps?.size ?: 0) ||
                    event.endIndex >= (_state.value.recipe?.steps?.size ?: 0) ||
                    event.endIndex == event.beginIndex
                ) {
                    return
                }

                val updatedRecipe = _state.value.recipe?.steps?.toMutableList()
                val removedStep = updatedRecipe?.removeAt(event.beginIndex)
                removedStep?.let {
                    updatedRecipe.add(event.endIndex, it)
                }
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
                        info = true,
                        editRecipe = it.recipe
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

            RecipeEvent.SaveEditRecipe -> {
                _state.update {
                    it.copy(
                        recipe = it.editRecipe
                    )
                }
                _state.value.recipe?.let {
                    upsertRecipe(it)
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

    fun onRecipeInfoEvent(event: RecipeInfoEvent) {
        when (event) {
            is RecipeInfoEvent.AddIngredient -> {
                val updatedIngredients = _state.value.editRecipe?.ingredients?.toMutableList()
                updatedIngredients?.add(
                    RecipeIngredient(
                        ingredient = ""
                    )
                )
                updatedIngredients?.toList()?.let { list ->
                    _state.update {
                        it.copy(
                            editRecipe = it.editRecipe?.copy(
                                ingredients = list
                            )
                        )
                    }
                }
            }

            is RecipeInfoEvent.RemoveIngredient -> {
                val updatedIngredients = _state.value.editRecipe?.ingredients?.filterNot {
                    it.id == event.id
                }
                updatedIngredients?.toList()?.let { list ->
                    _state.update {
                        it.copy(
                            editRecipe = it.editRecipe?.copy(
                                ingredients = list
                            )
                        )
                    }
                }
            }

            is RecipeInfoEvent.EditIngredient -> {
                val updatedIngredients = _state.value.editRecipe?.ingredients?.toMutableList()
                updatedIngredients?.set(event.index, event.ingredient)
                updatedIngredients?.toList()?.let { list ->
                    _state.update {
                        it.copy(
                            editRecipe = it.editRecipe?.copy(
                                ingredients = list
                            )
                        )
                    }
                }
            }

            is RecipeInfoEvent.EditRecipeName -> {
                _state.update {
                    it.copy(
                        editRecipe = it.editRecipe?.copy(
                            recipeName = event.recipeName
                        )
                    )
                }
            }

            is RecipeInfoEvent.EditRecipeServing -> {
                _state.update {
                    it.copy(
                        editRecipe = it.editRecipe?.copy(
                            serves = event.serves
                        )
                    )
                }
            }

            RecipeInfoEvent.ResetEditRecipe -> {
                _state.update {
                    it.copy(
                        editRecipe = it.recipe
                    )
                }
            }
        }
    }

    private fun upsertRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                recipeUC.upsertRecipe(recipe)
                _state.update { recipeState ->
                    recipeState.copy(
                        success = if (recipeState.info) RECIPE_UPDATED else RECIPE_ADDED
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
