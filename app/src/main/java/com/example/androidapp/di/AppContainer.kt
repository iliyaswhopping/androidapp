package com.example.androidapp.di

import com.example.androidapp.data.remote.RetrofitClient
import com.example.androidapp.data.repository.DeckRepositoryImpl
import com.example.androidapp.domain.repository.DeckRepository

// Manual dependency injection: one repository for the whole app,
// so all ViewModels share the same favourites and cache.
// To work without internet, change this line to: FakeDeckRepository()
object AppContainer {
    val deckRepository: DeckRepository by lazy { DeckRepositoryImpl(RetrofitClient.api) }
}
