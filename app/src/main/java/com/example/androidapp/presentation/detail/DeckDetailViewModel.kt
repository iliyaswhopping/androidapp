package com.example.androidapp.presentation.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
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

sealed interface DeckDetailUiState {
    data object Loading : DeckDetailUiState
    data class Success(val deck: Deck, val isFavourite: Boolean) : DeckDetailUiState
    data class Error(val message: String) : DeckDetailUiState
}

class DeckDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: DeckRepository
) : ViewModel() {

    // Navigation puts the "deckId" argument from "deck/{deckId}" into SavedStateHandle.
    private val deckId: Int = checkNotNull(savedStateHandle["deckId"])

    private val _uiState = MutableStateFlow<DeckDetailUiState>(DeckDetailUiState.Loading)
    val uiState: StateFlow<DeckDetailUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "init, deckId = $deckId")
        loadDeck()
        viewModelScope.launch {
            repository.favouriteIds.collect { ids ->
                _uiState.update { state ->
                    if (state is DeckDetailUiState.Success) state.copy(isFavourite = deckId in ids) else state
                }
            }
        }
    }

    fun onFavouriteClick() {
        repository.toggleFavourite(deckId)
    }

    fun onRetry() {
        loadDeck()
    }

    private fun loadDeck() {
        _uiState.value = DeckDetailUiState.Loading
        viewModelScope.launch {
            try {
                val deck = repository.getDeck(deckId)
                _uiState.value = if (deck != null) {
                    DeckDetailUiState.Success(deck, isFavourite = deckId in repository.favouriteIds.value)
                } else {
                    DeckDetailUiState.Error("Deck not found.")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = DeckDetailUiState.Error(e.toUserMessage())
            }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }

    companion object {
        private const val TAG = "DeckDetailViewModel"

        val Factory = viewModelFactory {
            initializer {
                DeckDetailViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = AppContainer.deckRepository
                )
            }
        }
    }
}
