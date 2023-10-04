package com.example.reciperoulette.viewModels.recipeViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reciperoulette.activities.recipeGeneratorActivity.userActions.Filter
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
class RecipeViewModel @Inject constructor(
    private val recipeRepo: RecipeRepositoryImpl,
    private val ingredientUC: IngredientsUseCases
): ViewModel() {
    companion object {
        const val UNKNOWN_ERROR = "An unknown error has occurred."
    }

    private val _state = MutableStateFlow(IngredientState())
    val state : StateFlow<IngredientState> = _state

    val selectedIngredients: MutableList<String> = mutableStateListOf()

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
                        searchName = event.name
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
                getIngredients(event.filter)
            }
            is IngredientEvent.RemoveIngredient -> {
                deleteIngredient(event.ingredient)
            }

            IngredientEvent.ClearError -> {
                _state.update { it.copy(
                    error = ""
                ) }
            }
        }
    }

    private fun getIngredients(filter: Filter = Filter()) {
        viewModelScope.launch {
            ingredientUC.getIngredients(filter)
                .collect { ingredientList ->
                _state.update {
                    it.copy(
                        loading = false,
                        ingredients = ingredientList
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
            getIngredients(_state.value.filter)
        }
    }

    private fun deleteIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            ingredientUC.deleteIngredient(ingredient)
            getIngredients(_state.value.filter)
        }
    }

    private fun verifyIngredient() {
        viewModelScope.launch {
            val response = ingredientUC.validateIngredient(_state.value.searchName)
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
                selectedIngredients
            )
        }
    }
}