package com.example.androidapp.presentation.favourites

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
import kotlinx.coroutines.launch

sealed interface FavouritesUiState {
    data object Loading : FavouritesUiState
    data class Success(val decks: List<Deck>) : FavouritesUiState // empty list = "No favourites yet"
    data class Error(val message: String) : FavouritesUiState
}

class FavouritesViewModel(
    private val repository: DeckRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavouritesUiState>(FavouritesUiState.Loading)
    val uiState: StateFlow<FavouritesUiState> = _uiState.asStateFlow()

    private var allDecks: List<Deck>? = null

    init {
        Log.d(TAG, "init")
        loadDecks()
        viewModelScope.launch {
            repository.favouriteIds.collect { showFavourites() }
        }
    }

    fun onRetry() {
        loadDecks()
    }

    private fun loadDecks() {
        _uiState.value = FavouritesUiState.Loading
        viewModelScope.launch {
            try {
                allDecks = repository.getDecks()
                showFavourites()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = FavouritesUiState.Error(e.toUserMessage())
            }
        }
    }

    private fun showFavourites() {
        val decks = allDecks ?: return
        val ids = repository.favouriteIds.value
        _uiState.value = FavouritesUiState.Success(decks.filter { it.id in ids })
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }

    companion object {
        private const val TAG = "FavouritesViewModel"

        val Factory = viewModelFactory {
            initializer { FavouritesViewModel(AppContainer.deckRepository) }
        }
    }
}
