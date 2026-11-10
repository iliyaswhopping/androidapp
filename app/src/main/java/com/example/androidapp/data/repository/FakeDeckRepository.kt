package com.example.androidapp.data.repository

import com.example.androidapp.data.fake.sampleDecks
import com.example.androidapp.domain.model.Deck
import com.example.androidapp.domain.repository.DeckRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException

// SIS4 repository: behaves like a server without the internet.
// Waits with delay() and can simulate an error with shouldFail = true.
// Used in unit tests; can also replace DeckRepositoryImpl in AppContainer.
class FakeDeckRepository(
    var shouldFail: Boolean = false,
    private val delayMillis: Long = 1_000
) : DeckRepository {

    private val _favouriteIds = MutableStateFlow<Set<Int>>(emptySet())
    override val favouriteIds: StateFlow<Set<Int>> = _favouriteIds.asStateFlow()

    override suspend fun getDecks(): List<Deck> {
        delay(delayMillis)
        if (shouldFail) throw IOException("Simulated network error")
        return sampleDecks
    }

    override suspend fun getDeck(id: Int): Deck? = getDecks().find { it.id == id }

    override fun toggleFavourite(deckId: Int) {
        _favouriteIds.update { ids -> if (deckId in ids) ids - deckId else ids + deckId }
    }
}
