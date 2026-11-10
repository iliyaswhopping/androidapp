package com.example.androidapp.presentation.decklist

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidapp.R
import com.example.androidapp.data.fake.sampleDecks
import com.example.androidapp.domain.model.Deck
import com.example.androidapp.presentation.components.CategoryChip
import com.example.androidapp.presentation.components.DeckCard
import com.example.androidapp.presentation.components.EmptyState
import com.example.androidapp.presentation.components.ErrorState
import com.example.androidapp.presentation.components.LoadingState
import com.example.androidapp.presentation.components.SectionHeader
import com.example.androidapp.presentation.theme.AndroidappTheme
import com.example.androidapp.presentation.theme.Spacing

// Route: the only place that knows about the ViewModel.
// It reads the state and passes it + event lambdas to the stateless screen.
@Composable
fun DeckListRoute(
    onDeckClick: (Deck) -> Unit,
    onFavouritesClick: () -> Unit,
    viewModel: DeckListViewModel = viewModel(factory = DeckListViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeckListScreen(
        uiState = uiState,
        onSearchChange = viewModel::onSearchChange,
        onCategorySelected = viewModel::onCategorySelected,
        onRetry = viewModel::onRetry,
        onDeckClick = onDeckClick,
        onFavouritesClick = onFavouritesClick
    )
}

// Stateless screen: gets uiState and lambdas, does not know the ViewModel exists.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen(
    uiState: DeckListUiState,
    onSearchChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onRetry: () -> Unit,
    onDeckClick: (Deck) -> Unit,
    onFavouritesClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FlashDeck") },
                actions = {
                    IconButton(onClick = onFavouritesClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_favorite),
                            contentDescription = "Open favourites"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (val state = uiState.loadState) {
            is DeckListLoadState.Loading -> LoadingState(modifier = contentModifier)

            is DeckListLoadState.Error -> ErrorState(
                message = state.message,
                onRetry = onRetry,
                modifier = contentModifier
            )

            is DeckListLoadState.Success -> DeckListContent(
                decks = state.decks,
                uiState = uiState,
                onSearchChange = onSearchChange,
                onCategorySelected = onCategorySelected,
                onDeckClick = onDeckClick,
                modifier = contentModifier
            )
        }
    }
}

@Composable
private fun DeckListContent(
    decks: List<Deck>,
    uiState: DeckListUiState,
    onSearchChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onDeckClick: (Deck) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchChange,
            label = { Text("Search decks") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.m, vertical = Spacing.s)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = Spacing.m),
            horizontalArrangement = Arrangement.spacedBy(Spacing.s)
        ) {
            items(uiState.categories) { category ->
                CategoryChip(
                    label = category,
                    selected = category == uiState.selectedCategory,
                    onClick = { onCategorySelected(category) }
                )
            }
        }

        SectionHeader(
            title = "Decks",
            subtitle = "${decks.size} decks",
            modifier = Modifier.padding(horizontal = Spacing.m, vertical = Spacing.s)
        )

        if (decks.isEmpty()) {
            EmptyState(
                title = "No decks found",
                message = "Nothing matches your search or category. Try something else.",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = Spacing.m, end = Spacing.m, bottom = Spacing.m),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                items(decks, key = { it.id }) { deck ->
                    DeckCard(
                        deck = deck,
                        isFavourite = deck.id in uiState.favouriteIds,
                        onClick = { onDeckClick(deck) }
                    )
                }
            }
        }
    }
}

// ---- Previews for the 4 states, built from fake UiState objects ----

private val previewCategories = listOf(ALL_CATEGORIES, "Languages", "Science", "Programming", "History")

@Composable
private fun PreviewScreen(uiState: DeckListUiState) {
    AndroidappTheme {
        DeckListScreen(
            uiState = uiState,
            onSearchChange = {},
            onCategorySelected = {},
            onRetry = {},
            onDeckClick = {},
            onFavouritesClick = {}
        )
    }
}

@Preview(showBackground = true, name = "1. Loading")
@Composable
private fun DeckListLoadingPreview() {
    PreviewScreen(DeckListUiState(loadState = DeckListLoadState.Loading))
}

@Preview(showBackground = true, name = "2. Content")
@Composable
private fun DeckListContentPreview() {
    PreviewScreen(
        DeckListUiState(
            loadState = DeckListLoadState.Success(sampleDecks),
            categories = previewCategories,
            favouriteIds = setOf(1, 5)
        )
    )
}

@Preview(showBackground = true, name = "3. Empty")
@Composable
private fun DeckListEmptyPreview() {
    PreviewScreen(
        DeckListUiState(
            loadState = DeckListLoadState.Success(emptyList()),
            searchQuery = "xyz",
            categories = previewCategories
        )
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "4. Error (dark)")
@Composable
private fun DeckListErrorPreview() {
    PreviewScreen(
        DeckListUiState(loadState = DeckListLoadState.Error("No internet connection. Check Wi-Fi and try again."))
    )
}
