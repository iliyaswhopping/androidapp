package com.example.androidapp.presentation.decklist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidapp.di.AppContainer
import com.example.androidapp.domain.model.Deck
import com.example.androidapp.domain.repository.DeckRepository
import com.example.androidapp.presentation.toUserMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckListViewModel(
    private val repository: DeckRepository
) : ViewModel() {

    // Private: only the ViewModel can change the state.
    private val _uiState = MutableStateFlow(DeckListUiState())
    // Public read-only: the screen can only read it.
    val uiState: StateFlow<DeckListUiState> = _uiState.asStateFlow()

    // Full list from the repository. The screen gets only the filtered part.
    private var allDecks: List<Deck> = emptyList()
    private var isLoaded = false

    init {
        Log.d(TAG, "init")
        loadDecks()
        // Favourites can change on the detail screen; keep the hearts in sync.
        viewModelScope.launch {
            repository.favouriteIds.collect { ids ->
                _uiState.update { it.copy(favouriteIds = ids) }
            }
        }
    }

    // ---- Events from the screen ----

    fun onSearchChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        showFilteredDecks()
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        showFilteredDecks()
    }

    fun onRetry() {
        loadDecks()
    }

    // ---- Logic ----

    private fun loadDecks() {
        _uiState.update { it.copy(loadState = DeckListLoadState.Loading) }
        viewModelScope.launch {
            try {
                allDecks = repository.getDecks()
                isLoaded = true
                val categories = listOf(ALL_CATEGORIES) + allDecks.map { it.category }.distinct()
                _uiState.update { it.copy(categories = categories) }
                showFilteredDecks()
            } catch (e: CancellationException) {
                throw e // the ViewModel is being destroyed, this is not an error
            } catch (e: Exception) {
                Log.d(TAG, "loadDecks failed", e)
                _uiState.update { it.copy(loadState = DeckListLoadState.Error(e.toUserMessage())) }
            }
        }
    }

    // Filtering and searching live here, not in the composable.
    private fun showFilteredDecks() {
        // While loading or on error there is nothing to filter yet.
        if (!isLoaded) return

        val state = _uiState.value
        val query = state.searchQuery.trim()
        val visible = allDecks
            .filter { state.selectedCategory == ALL_CATEGORIES || it.category == state.selectedCategory }
            .filter {
                query.isEmpty() ||
                    it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
            }
        _uiState.update { it.copy(loadState = DeckListLoadState.Success(visible)) }
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }

    companion object {
        private const val TAG = "DeckListViewModel"

        // Tells Compose how to create this ViewModel (it needs a repository).
        val Factory = viewModelFactory {
            initializer { DeckListViewModel(AppContainer.deckRepository) }
        }
    }
}
