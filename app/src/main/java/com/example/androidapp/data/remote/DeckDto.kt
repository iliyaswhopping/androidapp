package com.example.androidapp.data.remote

import com.google.gson.annotations.SerializedName

// DTO = "Data Transfer Object": exactly the shape of the JSON from the API.
// Fields are nullable because the server can forget a field;
// DeckRepositoryImpl turns these into the clean domain model Deck.

data class DeckDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("emoji") val emoji: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("cards") val cards: List<FlashcardDto>?
)

data class FlashcardDto(
    @SerializedName("front") val front: String?,
    @SerializedName("back") val back: String?
)
