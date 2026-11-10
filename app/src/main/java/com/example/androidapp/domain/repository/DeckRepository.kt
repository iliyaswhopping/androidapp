package com.example.androidapp.domain.repository

import com.example.androidapp.domain.model.Deck
import kotlinx.coroutines.flow.StateFlow

// The "contract" of the data layer. ViewModels depend on this interface,
// not on Retrofit. So we can swap DeckRepositoryImpl (real API)
// for FakeDeckRepository (previews, tests) without touching the screens.
interface DeckRepository {

    // Ids of decks the user marked with a heart. Every screen observes the same set.
    val favouriteIds: StateFlow<Set<Int>>

    suspend fun getDecks(): List<Deck>

    suspend fun getDeck(id: Int): Deck?

    fun toggleFavourite(deckId: Int)
}
