package com.example.androidapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // The JSON file api/decks.json lives in my GitHub repository.
    // GitHub serves the raw file at this address, so it works like a small read-only API.
    // !!! Replace YOUR_GITHUB_USERNAME and YOUR_REPO with your own. Must end with "/".
    private const val BASE_URL =
        "https://raw.githubusercontent.com/YOUR_GITHUB_USERNAME/YOUR_REPO/main/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON -> DeckDto
            .build()
    }

    // Retrofit creates the implementation of FlashDeckApi.
    val api: FlashDeckApi by lazy { retrofit.create(FlashDeckApi::class.java) }
}
