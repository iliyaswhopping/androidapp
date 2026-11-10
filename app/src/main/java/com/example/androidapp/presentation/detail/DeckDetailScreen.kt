package com.example.androidapp.presentation.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidapp.R
import com.example.androidapp.data.fake.sampleDecks
import com.example.androidapp.domain.model.Deck
import com.example.androidapp.presentation.components.EmptyState
import com.example.androidapp.presentation.components.ErrorState
import com.example.androidapp.presentation.components.FlashcardItem
import com.example.androidapp.presentation.components.LoadingState
import com.example.androidapp.presentation.components.SectionHeader
import com.example.androidapp.presentation.theme.AndroidappTheme
import com.example.androidapp.presentation.theme.Spacing

@Composable
fun DeckDetailRoute(
    onBackClick: () -> Unit,
    viewModel: DeckDetailViewModel = viewModel(factory = DeckDetailViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeckDetailScreen(
        uiState = uiState,
        onFavouriteClick = viewModel::onFavouriteClick,
        onRetry = viewModel::onRetry,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckDetailScreen(
    uiState: DeckDetailUiState,
    onFavouriteClick: () -> Unit,
    onRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = (uiState as? DeckDetailUiState.Success)?.deck?.title ?: "Deck",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (uiState is DeckDetailUiState.Success) {
                        IconButton(onClick = onFavouriteClick) {
                            Icon(
                                painter = painterResource(
                                    if (uiState.isFavourite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                                ),
                                contentDescription = if (uiState.isFavourite) "Remove from favourites" else "Add to favourites",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (uiState) {
            is DeckDetailUiState.Loading -> LoadingState(modifier = contentModifier)
            is DeckDetailUiState.Error -> ErrorState(
                message = uiState.message,
                onRetry = onRetry,
                modifier = contentModifier
            )
            is DeckDetailUiState.Success -> DeckDetailContent(
                deck = uiState.deck,
                onBackClick = onBackClick,
                modifier = contentModifier
            )
        }
    }
}

@Composable
private fun DeckDetailContent(
    deck: Deck,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(Spacing.m),
        verticalArrangement = Arrangement.spacedBy(Spacing.s)
    ) {
        item {
            Column {
                Text(text = deck.emoji, style = MaterialTheme.typography.displayMedium)
                Spacer(modifier = Modifier.height(Spacing.s))
                Text(
                    text = deck.title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${deck.category} • ${deck.cards.size} cards",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(Spacing.m))
                Text(text = deck.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(Spacing.m))
                SectionHeader(title = "Cards", subtitle = "Tap a card to flip it")
            }
        }

        if (deck.cards.isEmpty()) {
            item {
                EmptyState(
                    title = "This deck is empty",
                    message = "There are no cards in this deck yet."
                )
            }
        } else {
            items(deck.cards) { card ->
                FlashcardItem(card = card)
            }
        }

        item {
            Spacer(modifier = Modifier.height(Spacing.s))
            Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
                Text("Back to decks")
            }
        }
    }
}

@Composable
private fun PreviewScreen(uiState: DeckDetailUiState) {
    AndroidappTheme {
        DeckDetailScreen(uiState = uiState, onFavouriteClick = {}, onRetry = {}, onBackClick = {})
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun DeckDetailLoadingPreview() = PreviewScreen(DeckDetailUiState.Loading)

@Preview(showBackground = true, name = "Content")
@Composable
private fun DeckDetailContentPreview() =
    PreviewScreen(DeckDetailUiState.Success(sampleDecks[0], isFavourite = true))

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Empty deck (dark)")
@Composable
private fun DeckDetailEmptyPreview() =
    PreviewScreen(DeckDetailUiState.Success(sampleDecks.last(), isFavourite = false))

@Preview(showBackground = true, name = "Error")
@Composable
private fun DeckDetailErrorPreview() =
    PreviewScreen(DeckDetailUiState.Error("No internet connection. Check Wi-Fi and try again."))
