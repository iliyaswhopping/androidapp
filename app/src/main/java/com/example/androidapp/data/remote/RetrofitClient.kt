package com.example.androidapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://raw.githubusercontent.com/iliyaswhopping/androidapp/main/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON -> DeckDto
            .build()
    }

    // Retrofit creates the implementation of FlashDeckApi.
    val api: FlashDeckApi by lazy { retrofit.create(FlashDeckApi::class.java) }
}
