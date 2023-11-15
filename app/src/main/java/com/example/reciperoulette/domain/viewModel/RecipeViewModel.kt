package com.example.reciperoulette.domain.viewModel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reciperoulette.common.Resource
import com.example.reciperoulette.data.local.recipes.RecipeIngredient
import com.example.reciperoulette.data.local.recipes.RecipeStep
import com.example.reciperoulette.data.local.recipes.entities.Recipe
import com.example.reciperoulette.domain.useCase.recipesUC.RecipesUseCases
import com.example.reciperoulette.presentation.screens.recipeScreen.userActions.RecipeEvent
import com.example.reciperoulette.presentation.screens.recipeScreen.userActions.RecipeInfoEvent
import com.example.reciperoulette.presentation.screens.recipeScreen.userActions.RecipeState
import com.example.reciperoulette.presentation.screens.recipeScreen.userActions.RecipeStepEvent
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
        const val RECIPE_ADDED = "Recipe was successfully added to your recipe list."
        const val RECIPE_UPDATED = "Recipe was successfully updated."
        const val INVALID_RECIPE_INFO = "Invalid format. " +
            "Please make sure recipe name and ingredients are not empty."
        const val INVALID_RECIPE_STEP = "Invalid format. " +
            "Please make sure step instructions are not empty."

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
                    error = UNKNOWN_ERROR,
                    showDialog = true
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
                val updatedSteps = _state.value.recipe?.steps?.toMutableList()
                updatedSteps?.add(
                    RecipeStep()
                )
                updatedSteps?.toList()?.let { list ->
                    _state.update {
                        it.copy(
                            isEditing = true,
                            editStep = list.size - 1,
                            editRecipe = it.recipe?.copy(
                                steps = list
                            ),
                            addStep = true
                        )
                    }
                }
            }

            is RecipeEvent.EditRecipeInfo -> {
                _state.update {
                    it.copy(
                        isEditing = event.isEditing,
                        editRecipe = it.recipe
                    )
                }
            }

            is RecipeEvent.EditRecipeStep -> {
                _state.update {
                    it.copy(
                        isEditing = event.isEditing,
                        editStep = event.recipeStep,
                        editRecipe = it.recipe,
                        addStep = false
                    )
                }
            }

            RecipeEvent.DismissStep -> {
                _state.update {
                    it.copy(
                        editStep = null,
                        isEditing = false
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
                        info = false,
                        isEditing = false
                    )
                }
            }

            RecipeEvent.ClearError -> {
                _state.update {
                    it.copy(
                        error = "",
                        showDialog = false
                    )
                }
            }

            RecipeEvent.ClearSuccess -> {
                _state.update {
                    it.copy(
                        success = "",
                        showDialog = false
                    )
                }
            }

            RecipeEvent.SaveEditRecipeInfo -> {
                val isValid = checkRecipeInfoEditValidity()

                if (!isValid) {
                    _state.update {
                        it.copy(
                            error = INVALID_RECIPE_INFO
                        )
                    }
                    return
                }
                _state.value.recipe?.let {
                    upsertRecipe(it)
                }
                _state.update {
                    it.copy(
                        recipe = it.editRecipe,
                        isEditing = false,
                        info = false
                    )
                }
            }

            is RecipeEvent.SaveEditRecipeStep -> {
                val isValid = checkRecipeStepValidity()

                if (!isValid) {
                    _state.update {
                        it.copy(
                            error = INVALID_RECIPE_STEP
                        )
                    }
                    return
                }
                _state.value.recipe?.let {
                    upsertRecipe(it)
                }
                _state.update {
                    it.copy(
                        recipe = it.editRecipe,
                        isEditing = false,
                        editStep = null,
                        addStep = false
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
                updatedIngredients?.forEachIndexed { index, recipeIngredient ->
                    if (recipeIngredient.id == event.id) {
                        updatedIngredients[index] = RecipeIngredient(
                            event.id,
                            event.ingredient
                        )
                        return@forEachIndexed
                    }
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
        }
    }

    fun onRecipeStepEvent(event: RecipeStepEvent) {
        when (event) {
            is RecipeStepEvent.EditStepNumber -> {
                val updatedSteps = _state.value.editRecipe?.steps?.toMutableList()
                val removedStep = _state.value.editStep?.let {
                    updatedSteps?.removeAt(it)
                }
                removedStep?.let {
                    updatedSteps?.add(
                        event.step,
                        removedStep
                    )
                }
                updatedSteps?.toList()?.let { list ->
                    _state.update {
                        it.copy(
                            editRecipe = it.editRecipe?.copy(
                                steps = list
                            ),
                            editStep = event.step
                        )
                    }
                }
            }

            is RecipeStepEvent.EditMinutes -> {
                val updatedSteps = _state.value.editRecipe?.steps?.toMutableList()
                _state.value.editStep?.let { stepNumber ->
                    updatedSteps?.set(
                        stepNumber,
                        RecipeStep(
                            instructions = updatedSteps[stepNumber].instructions,
                            minutes = event.minutes
                        )
                    )
                }
                updatedSteps?.toList()?.let { list ->
                    _state.update {
                        it.copy(
                            editRecipe = it.editRecipe?.copy(
                                steps = list
                            )
                        )
                    }
                }
            }

            is RecipeStepEvent.EditInstructions -> {
                val updatedSteps = _state.value.editRecipe?.steps?.toMutableList()
                _state.value.editStep?.let { stepNumber ->
                    updatedSteps?.set(
                        stepNumber,
                        RecipeStep(
                            instructions = event.instructions,
                            minutes = updatedSteps[stepNumber].minutes
                        )
                    )
                }
                updatedSteps?.toList()?.let { list ->
                    _state.update {
                        it.copy(
                            editRecipe = it.editRecipe?.copy(
                                steps = list
                            )
                        )
                    }
                }
            }
        }
    }

    private fun checkRecipeInfoEditValidity(): Boolean {
        _state.value.editRecipe?.ingredients?.forEach {
            if (it.ingredient.isBlank()) {
                return false
            }
        }

        if (_state.value.editRecipe?.recipeName?.isBlank() == true) {
            return false
        }

        return true
    }

    private fun checkRecipeStepValidity(): Boolean {
        _state.value.editStep?.let {
            if (_state.value.editRecipe?.steps?.get(it)?.instructions?.isBlank() == true) {
                return false
            }
        } ?: return false

        return true
    }

    private fun upsertRecipe(recipe: Recipe) {
        val successMessage = if (_state.value.info || state.value.editStep != null) {
            RECIPE_UPDATED
        } else {
            RECIPE_ADDED
        }
        viewModelScope.launch {
            try {
                recipeUC.upsertRecipe(recipe)
                _state.update { recipeState ->
                    recipeState.copy(
                        success = successMessage
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
                                error = it.message?.ifEmpty { UNKNOWN_ERROR } ?: UNKNOWN_ERROR,
                                showDialog = true
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
