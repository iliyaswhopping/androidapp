package com.example.androidapp.domain.model

// Domain model: plain Kotlin classes. No Retrofit, no Gson, no Android here.
// The screens and ViewModels only know these classes.

data class Flashcard(
    val front: String,
    val back: String
)

data class Deck(
    val id: Int,
    val title: String,
    val category: String,
    val emoji: String,
    val description: String,
    val cards: List<Flashcard>
)
