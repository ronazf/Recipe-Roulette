package com.example.reciperoulette.viewModels.recipeViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reciperoulette.activities.recipeGeneratorActivity.userActions.IngredientEvent
import com.example.reciperoulette.activities.recipeGeneratorActivity.userActions.IngredientState
import com.example.reciperoulette.api.response.Resource
import com.example.reciperoulette.database.ingredients.CategoryDetail
import com.example.reciperoulette.database.ingredients.InvalidCategoryException
import com.example.reciperoulette.repositories.recipeRepository.RecipeRepositoryImpl
import com.example.reciperoulette.database.ingredients.entities.Ingredient
import com.example.reciperoulette.use_case.ingredientsUC.IngredientsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
// TODO: check state race condition with multiple updates
class RecipeViewModel @Inject constructor(
    private val recipeRepo: RecipeRepositoryImpl,
    private val ingredientUC: IngredientsUseCases
) : ViewModel() {
    companion object {
        const val UNKNOWN_ERROR = "An unknown error has occurred."
        const val DUPLICATE_INGREDIENT = "This ingredient is already in your ingredient list."
        const val INGREDIENT_ADDED =
            "Custom ingredient was successfully added to your ingredient list."
    }

    private val _state = MutableStateFlow(IngredientState())
    val state: StateFlow<IngredientState> = _state

    init {
        _state.update {
            it.copy(loading = true)
        }
        getIngredients()
    }

    fun onIngredientEvent(event: IngredientEvent) {
        when (event) {
            is IngredientEvent.AddIngredient -> {
                _state.update {
                    it.copy(
                        verifyingIngredient = true,
                        ingredientName = event.name
                    )
                }
                verifyIngredient()
            }

            is IngredientEvent.FilterIngredients -> {
                _state.update {
                    it.copy(
                        filter = event.filter
                    )
                }
                getIngredients()
            }

            is IngredientEvent.RemoveIngredient -> {
                _state.value.ingredients.find {
                    it.ingredientName == event.ingredientName
                }?.let {
                    deleteIngredient(it)
                }
            }

            is IngredientEvent.SelectIngredient -> {
                val updatedSelection = _state.value.selectedIngredients.toMutableList()
                updatedSelection.add(event.name)
                updatedSelection.sort()
                _state.update {
                    it.copy(
                        selectedIngredients = updatedSelection.toList()
                    )
                }
            }

            is IngredientEvent.RemoveSelectedIngredient -> {
                val updatedSelection = _state.value.selectedIngredients.toMutableList()
                updatedSelection.remove(event.name)
                updatedSelection.sort()
                _state.update {
                    it.copy(
                        selectedIngredients = updatedSelection.toList()
                    )
                }
            }

            is IngredientEvent.SearchIngredient -> {
                _state.update {
                    it.copy(
                        searchText = event.searchText
                    )
                }
                getIngredients()
            }

            IngredientEvent.ClearSearch -> {
                _state.update {
                    it.copy(
                        searchText = ""
                    )
                }
                getIngredients()
            }

            IngredientEvent.ClearError -> {
                _state.update {
                    it.copy(
                        error = ""
                    )
                }
            }

            IngredientEvent.ClearSuccess -> {
                _state.update {
                    it.copy(
                        success = ""
                    )
                }
            }
        }
    }

    private fun getIngredients() {
        viewModelScope.launch {
            ingredientUC.getIngredients(_state.value.filter, _state.value.searchText)
                .collect { ingredientList ->
                    _state.update {
                        it.copy(
                            loading = false,
                            ingredients = ingredientList,
                            mappedIngredients = ingredientList
                                .groupBy { ingredient ->
                                    CategoryDetail.values().find { category ->
                                        category.id == ingredient.categoryId.toInt()
                                    } ?: throw InvalidCategoryException("")
                                }
                        )
                    }
                }
        }
    }

    private fun upsertIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            ingredientUC.upsertIngredient(ingredient)
            getIngredients()
        }
    }

    private fun deleteIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            Log.d("success", "removing ingredient: " + ingredient)
            ingredientUC.deleteIngredient(ingredient)
            getIngredients()
        }
    }

    private fun verifyIngredient() {
        viewModelScope.launch {
            if (_state.value.ingredients
                    .map { it.ingredientName.lowercase() }
                    .contains(_state.value.ingredientName.lowercase())
            ) {
                _state.update { ingredientState ->
                    ingredientState.copy(
                        verifyingIngredient = false,
                        error = DUPLICATE_INGREDIENT
                    )
                }
                return@launch
            }
            ingredientUC.validateIngredient(_state.value.ingredientName).collect {
                when (it) {
                    is Resource.Error -> {
                        _state.update { ingredientState ->
                            ingredientState.copy(
                                verifyingIngredient = false,
                                error = it.message ?: UNKNOWN_ERROR
                            )
                        }
                    }

                    is Resource.Loading -> {
                        if (!_state.value.verifyingIngredient) {
                            _state.update { ingredientState ->
                                ingredientState.copy(
                                    verifyingIngredient = true
                                )
                            }
                        }
                    }

                    is Resource.Success -> {
                        _state.update { ingredientState ->
                            ingredientState.copy(
                                verifyingIngredient = false,
                                success = INGREDIENT_ADDED
                            )
                        }
                        it.data?.let { ingredient ->
                            upsertIngredient(ingredient)
                        }
                    }
                }
            }
        }
    }

    private fun getRecipe() {
        viewModelScope.launch {
            val response = recipeRepo.getRecipe(
                _state.value.selectedIngredients
            )
        }
    }
}