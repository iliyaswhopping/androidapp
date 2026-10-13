package com.example.androidapp.ui.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidapp.R
import com.example.androidapp.data.Deck
import com.example.androidapp.data.decks
import com.example.androidapp.ui.components.EmptyState
import com.example.androidapp.ui.components.FlashcardItem
import com.example.androidapp.ui.components.SectionHeader
import com.example.androidapp.ui.theme.AndroidappTheme
import com.example.androidapp.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckDetailScreen(
    deck: Deck,
    isFavourite: Boolean,
    onFavouriteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = deck.title,
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
                    IconButton(onClick = onFavouriteClick) {
                        Icon(
                            painter = painterResource(
                                if (isFavourite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                            ),
                            contentDescription = if (isFavourite) "Remove from favourites" else "Add to favourites",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(Spacing.m),
            verticalArrangement = Arrangement.spacedBy(Spacing.s)
        ) {
            item {
                Column {
                    Text(
                        text = deck.emoji,
                        style = MaterialTheme.typography.displayMedium
                    )
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
                    Text(
                        text = deck.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
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
                Button(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to decks")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeckDetailScreenPreview() {
    AndroidappTheme {
        DeckDetailScreen(deck = decks[0], isFavourite = true, onFavouriteClick = {}, onBackClick = {})
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark, empty deck")
@Composable
private fun DeckDetailScreenDarkPreview() {
    AndroidappTheme {
        DeckDetailScreen(deck = decks.last(), isFavourite = false, onFavouriteClick = {}, onBackClick = {})
    }
}
