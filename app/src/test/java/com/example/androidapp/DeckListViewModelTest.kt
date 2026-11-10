package com.example.androidapp

import com.example.androidapp.data.repository.FakeDeckRepository
import com.example.androidapp.presentation.decklist.DeckListLoadState
import com.example.androidapp.presentation.decklist.DeckListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeckListViewModelTest {

    // viewModelScope uses Dispatchers.Main; in unit tests we replace it with a test dispatcher.
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() = Dispatchers.setMain(testDispatcher)

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun onCategorySelected_showsOnlyDecksOfThatCategory() = runTest(testDispatcher) {
        val viewModel = DeckListViewModel(FakeDeckRepository(delayMillis = 0))
        advanceUntilIdle() // let loadDecks() finish

        viewModel.onCategorySelected("Science")

        val loadState = viewModel.uiState.value.loadState as DeckListLoadState.Success
        assertTrue(loadState.decks.isNotEmpty())
        assertTrue(loadState.decks.all { it.category == "Science" })
    }

    @Test
    fun repositoryFails_stateIsError() = runTest(testDispatcher) {
        val viewModel = DeckListViewModel(FakeDeckRepository(shouldFail = true, delayMillis = 0))
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.loadState is DeckListLoadState.Error)
    }

    @Test
    fun onSearchChange_filtersByTitle() = runTest(testDispatcher) {
        val viewModel = DeckListViewModel(FakeDeckRepository(delayMillis = 0))
        advanceUntilIdle()

        viewModel.onSearchChange("kotlin")

        val loadState = viewModel.uiState.value.loadState as DeckListLoadState.Success
        assertEquals(listOf("Kotlin Basics"), loadState.decks.map { it.title })
    }
}
