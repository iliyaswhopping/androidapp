package com.example.androidapp.presentation.favourites

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.androidapp.presentation.components.DeckCard
import com.example.androidapp.presentation.components.EmptyState
import com.example.androidapp.presentation.components.ErrorState
import com.example.androidapp.presentation.components.LoadingState
import com.example.androidapp.presentation.components.SectionHeader
import com.example.androidapp.presentation.theme.AndroidappTheme
import com.example.androidapp.presentation.theme.Spacing

@Composable
fun FavouritesRoute(
    onDeckClick: (Deck) -> Unit,
    onBackClick: () -> Unit,
    viewModel: FavouritesViewModel = viewModel(factory = FavouritesViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavouritesScreen(
        uiState = uiState,
        onDeckClick = onDeckClick,
        onRetry = viewModel::onRetry,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    uiState: FavouritesUiState,
    onDeckClick: (Deck) -> Unit,
    onRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favourites") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (uiState) {
            is FavouritesUiState.Loading -> LoadingState(modifier = contentModifier)

            is FavouritesUiState.Error -> ErrorState(
                message = uiState.message,
                onRetry = onRetry,
                modifier = contentModifier
            )

            is FavouritesUiState.Success -> if (uiState.decks.isEmpty()) {
                EmptyState(
                    title = "No favourites yet",
                    message = "Open a deck and tap the heart to save it here.",
                    modifier = contentModifier
                )
            } else {
                LazyColumn(
                    modifier = contentModifier,
                    contentPadding = PaddingValues(Spacing.m),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s)
                ) {
                    item {
                        SectionHeader(title = "Saved decks", subtitle = "${uiState.decks.size} decks")
                    }
                    items(uiState.decks, key = { it.id }) { deck ->
                        DeckCard(deck = deck, isFavourite = true, onClick = { onDeckClick(deck) })
                    }
                }
            }
        }
    }
}

@Composable
private fun PreviewScreen(uiState: FavouritesUiState) {
    AndroidappTheme {
        FavouritesScreen(uiState = uiState, onDeckClick = {}, onRetry = {}, onBackClick = {})
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun FavouritesLoadingPreview() = PreviewScreen(FavouritesUiState.Loading)

@Preview(showBackground = true, name = "Content")
@Composable
private fun FavouritesContentPreview() = PreviewScreen(FavouritesUiState.Success(sampleDecks.take(3)))

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Empty (dark)")
@Composable
private fun FavouritesEmptyPreview() = PreviewScreen(FavouritesUiState.Success(emptyList()))

@Preview(showBackground = true, name = "Error")
@Composable
private fun FavouritesErrorPreview() =
    PreviewScreen(FavouritesUiState.Error("No internet connection. Check Wi-Fi and try again."))
