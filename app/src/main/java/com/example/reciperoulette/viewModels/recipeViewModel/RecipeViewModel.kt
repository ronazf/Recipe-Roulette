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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
// TODO: check state race condition with multiple updates
class RecipeViewModel @Inject constructor(
    private val recipeRepo: RecipeRepositoryImpl,
    private val ingredientUC: IngredientsUseCases
): ViewModel() {
    companion object {
        const val UNKNOWN_ERROR = "An unknown error has occurred."
    }

    private val _state = MutableStateFlow(IngredientState())
    val state : StateFlow<IngredientState> = _state

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
                        filter = event.filter,
                        loading = true
                    )
                }
                getIngredients()
            }
            is IngredientEvent.RemoveIngredient -> {
                deleteIngredient(event.ingredient)
            }

            is IngredientEvent.SelectIngredient -> {
                val updatedSelection = _state.value.selectedIngredients.toMutableList()
                updatedSelection.add(event.name)
                updatedSelection.sort()
                _state.update { it.copy(
                    selectedIngredients = updatedSelection.toList()
                ) }
            }

            is IngredientEvent.RemoveSelectedIngredient -> {
                val updatedSelection = _state.value.selectedIngredients.toMutableList()
                updatedSelection.remove(event.name)
                updatedSelection.sort()
                _state.update { it.copy(
                    selectedIngredients = updatedSelection.toList()
                ) }
            }

            is IngredientEvent.SearchIngredient -> {
                _state.update { it.copy(
                    searchText = event.searchText
                ) }
                getIngredients()
            }

            IngredientEvent.ClearSearch -> {
                _state.update { it.copy(
                    searchText = ""
                ) }
                getIngredients()
            }

            IngredientEvent.ClearError -> {
                _state.update { it.copy(
                    error = ""
                ) }
            }
        }
    }

    private fun getIngredients() {
        viewModelScope.launch {
            ingredientUC.getIngredients(_state.value.filter, _state.value.searchText)
                .collect { ingredientList ->
                _state.update {
                    if (_state.value.searchText.isEmpty()) {
                        it.copy(
                            loading = false,
                            mappedIngredients = ingredientList
                                .groupBy { ingredient ->
                                    CategoryDetail.values().find { category ->
                                        category.id == ingredient.categoryId.toInt()
                                    } ?: throw InvalidCategoryException("")
                                }
                        )
                    } else {
                        Log.d("Succss", ingredientList.toString())
                        it.copy(
                            loading = false,
                            ingredients = ingredientList
                        )
                    }
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
            ingredientUC.deleteIngredient(ingredient)
            getIngredients()
        }
    }

    private fun verifyIngredient() {
        viewModelScope.launch {
            val response = ingredientUC.validateIngredient(_state.value.ingredientName)
            response.onEach {
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
                        _state.update {ingredientState ->
                            ingredientState.copy(
                                verifyingIngredient = false,
                                loading = true
                            )
                        }
                        it.data?.let { ingredient -> upsertIngredient(ingredient) }
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