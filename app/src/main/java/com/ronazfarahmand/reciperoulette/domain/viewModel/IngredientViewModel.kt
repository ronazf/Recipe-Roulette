package com.ronazfarahmand.reciperoulette.domain.viewModel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronazfarahmand.reciperoulette.common.Resource
import com.ronazfarahmand.reciperoulette.data.local.ingredients.details.CategoryDetail
import com.ronazfarahmand.reciperoulette.data.local.ingredients.details.InvalidCategoryException
import com.ronazfarahmand.reciperoulette.data.local.ingredients.entities.Ingredient
import com.ronazfarahmand.reciperoulette.domain.useCase.ingredientsUC.IngredientsUseCases
import com.ronazfarahmand.reciperoulette.presentation.screens.ingredientScreen.userActions.Filter
import com.ronazfarahmand.reciperoulette.presentation.screens.ingredientScreen.userActions.IngredientEvent
import com.ronazfarahmand.reciperoulette.presentation.screens.ingredientScreen.userActions.IngredientState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
// TODO: check state race condition with multiple updates
@OptIn(ExperimentalCoroutinesApi::class)
class IngredientViewModel @Inject constructor(
    private val ingredientUC: IngredientsUseCases
) : ViewModel() {
    companion object {
        const val UNKNOWN_ERROR = "An unknown error has occurred."
        const val DUPLICATE_INGREDIENT = "This ingredient is already in your ingredient list."
        const val INGREDIENT_ADDED =
            "Custom ingredient was successfully added to your ingredient list."
        private const val SAFE_WAIT = 5000L
    }

    private val _searchText = MutableStateFlow("")
    private val _filter = MutableStateFlow(Filter())
    private val _ingredients = combine(_searchText, _filter) { searchText, filter ->
        Pair(searchText, filter)
    }.flatMapLatest { (searchText, filter) ->
        ingredientUC.getIngredients(filter = filter, searchText = searchText)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(IngredientState())
    val state = combine(
        _state,
        _searchText,
        _filter,
        _ingredients
    ) { state, searchText, filter, ingredients ->
        state.copy(
            ingredients = ingredients,
            mappedIngredients = ingredients
                .groupBy { ingredient ->
                    CategoryDetail.values().find { category ->
                        category.id == ingredient.categoryId.toInt()
                    } ?: throw InvalidCategoryException("")
                },
            searchText = searchText,
            filter = filter
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(SAFE_WAIT), IngredientState())

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
                _filter.value = event.filter
            }

            is IngredientEvent.RemoveIngredient -> {
                _ingredients.value.find {
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
                _searchText.value = event.searchText
            }

            IngredientEvent.ClearSearch -> {
                _searchText.value = ""
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

    private fun upsertIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            try {
                ingredientUC.upsertIngredient(ingredient)
                _state.update { ingredientState ->
                    ingredientState.copy(
                        success = INGREDIENT_ADDED
                    )
                }
            } catch (e: SQLiteConstraintException) {
                _state.update { ingredientState ->
                    ingredientState.copy(
                        error = DUPLICATE_INGREDIENT
                    )
                }
            }
        }
    }

    private fun deleteIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            ingredientUC.deleteIngredient(ingredient)
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
                                error = it.message?.ifEmpty { UNKNOWN_ERROR } ?: UNKNOWN_ERROR
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
                                verifyingIngredient = false
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
}
