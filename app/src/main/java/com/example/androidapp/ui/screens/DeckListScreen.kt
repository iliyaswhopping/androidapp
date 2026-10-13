package com.example.androidapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidapp.R
import com.example.androidapp.data.Deck
import com.example.androidapp.data.categories
import com.example.androidapp.data.decks
import com.example.androidapp.ui.components.CategoryChip
import com.example.androidapp.ui.components.DeckCard
import com.example.androidapp.ui.components.EmptyState
import com.example.androidapp.ui.components.SectionHeader
import com.example.androidapp.ui.theme.AndroidappTheme
import com.example.androidapp.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen(
    decks: List<Deck>,
    favouriteIds: Set<Int>,
    onDeckClick: (Deck) -> Unit,
    onFavouritesClick: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredDecks = if (selectedCategory == "All") {
        decks
    } else {
        decks.filter { it.category == selectedCategory }
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = Spacing.m),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                items(categories) { category ->
                    CategoryChip(
                        label = category,
                        selected = category == selectedCategory,
                        onClick = { selectedCategory = category }
                    )
                }
            }

            SectionHeader(
                title = "Decks",
                subtitle = "${filteredDecks.size} decks",
                modifier = Modifier.padding(horizontal = Spacing.m, vertical = Spacing.s)
            )

            if (filteredDecks.isEmpty()) {
                EmptyState(
                    title = "No decks here yet",
                    message = "There are no decks in \"$selectedCategory\". Try another category.",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = Spacing.m,
                        end = Spacing.m,
                        bottom = Spacing.m
                    ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s)
                ) {
                    items(filteredDecks, key = { it.id }) { deck ->
                        DeckCard(
                            deck = deck,
                            isFavourite = deck.id in favouriteIds,
                            onClick = { onDeckClick(deck) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeckListScreenPreview() {
    AndroidappTheme {
        DeckListScreen(decks = decks, favouriteIds = setOf(1, 5), onDeckClick = {}, onFavouritesClick = {})
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun DeckListScreenDarkPreview() {
    AndroidappTheme {
        DeckListScreen(decks = decks, favouriteIds = setOf(1, 5), onDeckClick = {}, onFavouritesClick = {})
    }
}
