package com.ronazfarahmand.reciperoulette.domain.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronazfarahmand.reciperoulette.data.local.recipes.entities.Recipe
import com.ronazfarahmand.reciperoulette.domain.useCase.recipesUC.RecipeLibraryUseCases
import com.ronazfarahmand.reciperoulette.presentation.screens.libraryScreen.userActions.LibraryEvent
import com.ronazfarahmand.reciperoulette.presentation.screens.libraryScreen.userActions.LibraryState
import com.ronazfarahmand.reciperoulette.presentation.screens.libraryScreen.userActions.RecipeFilter
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
@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModel @Inject constructor(
    private val libraryUC: RecipeLibraryUseCases
) : ViewModel() {

    companion object {
        private const val RECIPE_DELETED = "Recipe was successfully removed from your recipe list."
        private const val SAFE_WAIT = 5000L
    }

    private val _searchText = MutableStateFlow("")
    private val _filter = MutableStateFlow(RecipeFilter())
    private val _recipes = combine(_searchText, _filter) { searchText, filter ->
        Pair(searchText, filter)
    }.flatMapLatest { (searchText, filter) ->
        libraryUC.getRecipes(searchText = searchText, filter = filter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(LibraryState())
    val state = combine(
        _state,
        _searchText,
        _filter,
        _recipes
    ) { state, searchText, filter, recipes ->
        state.copy(
            recipes = recipes,
            searchText = searchText,
            filter = filter
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(SAFE_WAIT), LibraryState())

    fun onLibraryEvent(event: LibraryEvent) {
        when (event) {
            is LibraryEvent.DeleteRecipe -> {
                deleteRecipe(event.recipe)
            }
            is LibraryEvent.FilterRecipes -> {
                _filter.value = event.filter
            }
            is LibraryEvent.SearchRecipe -> {
                _searchText.value = event.searchText
            }
            LibraryEvent.ClearSearch -> {
                _searchText.value = ""
            }
            is LibraryEvent.SetFavourite -> {
                setRecipeFavourite(event.id)
            }
            LibraryEvent.ClearError -> {
                _state.update {
                    it.copy(
                        error = ""
                    )
                }
            }
            LibraryEvent.ClearSuccess -> {
                _state.update {
                    it.copy(
                        success = ""
                    )
                }
            }
        }
    }

    private fun setRecipeFavourite(id: Long) {
        viewModelScope.launch {
            libraryUC.setRecipeFavourite(id)
        }
    }

    private fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            libraryUC.deleteRecipe(recipe)
            _state.update { libraryState ->
                libraryState.copy(
                    success = RECIPE_DELETED
                )
            }
        }
    }
}
