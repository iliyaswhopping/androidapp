package com.example.androidapp.data.remote

import retrofit2.http.GET

// Retrofit interface: describes the endpoints. Retrofit writes the implementation for us.
// Full URL = RetrofitClient.BASE_URL + "api/decks.json"
interface FlashDeckApi {

    @GET("api/decks.json")
    suspend fun getDecks(): List<DeckDto>
}
