package com.example.androidapp.data.repository

import com.example.androidapp.data.remote.DeckDto
import com.example.androidapp.data.remote.FlashDeckApi
import com.example.androidapp.data.remote.FlashcardDto
import com.example.androidapp.domain.model.Deck
import com.example.androidapp.domain.model.Flashcard
import com.example.androidapp.domain.repository.DeckRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// The real repository: gets decks from the API and turns DeckDto into Deck.
class DeckRepositoryImpl(
    private val api: FlashDeckApi
) : DeckRepository {

    private val _favouriteIds = MutableStateFlow<Set<Int>>(emptySet())
    override val favouriteIds: StateFlow<Set<Int>> = _favouriteIds.asStateFlow()

    // Last list we downloaded, so the detail screen does not need a new request.
    private var cachedDecks: List<Deck>? = null

    override suspend fun getDecks(): List<Deck> {
        // 1. Call the API (throws IOException if there is no internet).
        val dtos = api.getDecks()
        // 2. Turn the API class into our model class.
        val decks = dtos.mapNotNull { it.toDeck() }
        cachedDecks = decks
        return decks
    }

    override suspend fun getDeck(id: Int): Deck? {
        val decks = cachedDecks ?: getDecks()
        return decks.find { it.id == id }
    }

    override fun toggleFavourite(deckId: Int) {
        _favouriteIds.update { ids ->
            if (deckId in ids) ids - deckId else ids + deckId
        }
    }
}

// Mapper: DeckDto (API) -> Deck (domain). A deck without an id is skipped.
private fun DeckDto.toDeck(): Deck? {
    if (id == null) return null
    return Deck(
        id = id,
        title = title ?: "Untitled deck",
        category = category ?: "Other",
        emoji = emoji ?: "📚",
        description = description ?: "",
        cards = cards.orEmpty().map { it.toFlashcard() }
    )
}

private fun FlashcardDto.toFlashcard(): Flashcard =
    Flashcard(front = front ?: "", back = back ?: "")
