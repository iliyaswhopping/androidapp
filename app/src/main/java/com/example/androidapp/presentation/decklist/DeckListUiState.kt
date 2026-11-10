package com.example.androidapp.presentation.decklist

import com.example.androidapp.domain.model.Deck

const val ALL_CATEGORIES = "All"

// "Order board": everything the deck list screen needs to draw itself.
// Only val properties; the ViewModel changes it with copy().
data class DeckListUiState(
    val loadState: DeckListLoadState = DeckListLoadState.Loading, // starts as Loading
    val searchQuery: String = "",
    val selectedCategory: String = ALL_CATEGORIES,
    val categories: List<String> = listOf(ALL_CATEGORIES),
    val favouriteIds: Set<Int> = emptySet()
)

// Loading -> Success(items) or Error(message).
// "Empty" is Success with an empty list (no decks match the search/filter).
sealed interface DeckListLoadState {
    data object Loading : DeckListLoadState
    data class Success(val decks: List<Deck>) : DeckListLoadState
    data class Error(val message: String) : DeckListLoadState
}
