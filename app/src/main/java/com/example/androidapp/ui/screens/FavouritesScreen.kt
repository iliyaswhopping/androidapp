package com.example.androidapp.ui.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidapp.R
import com.example.androidapp.data.Deck
import com.example.androidapp.data.decks
import com.example.androidapp.ui.components.DeckCard
import com.example.androidapp.ui.components.EmptyState
import com.example.androidapp.ui.components.SectionHeader
import com.example.androidapp.ui.theme.AndroidappTheme
import com.example.androidapp.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    favouriteDecks: List<Deck>,
    onDeckClick: (Deck) -> Unit,
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
        if (favouriteDecks.isEmpty()) {
            EmptyState(
                title = "No favourites yet",
                message = "Open a deck and tap the heart to save it here.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(Spacing.m),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                item {
                    SectionHeader(
                        title = "Saved decks",
                        subtitle = "${favouriteDecks.size} decks"
                    )
                }
                items(favouriteDecks, key = { it.id }) { deck ->
                    DeckCard(
                        deck = deck,
                        isFavourite = true,
                        onClick = { onDeckClick(deck) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavouritesScreenPreview() {
    AndroidappTheme {
        FavouritesScreen(favouriteDecks = decks.take(3), onDeckClick = {}, onBackClick = {})
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark, empty")
@Composable
private fun FavouritesScreenEmptyDarkPreview() {
    AndroidappTheme {
        FavouritesScreen(favouriteDecks = emptyList(), onDeckClick = {}, onBackClick = {})
    }
}
