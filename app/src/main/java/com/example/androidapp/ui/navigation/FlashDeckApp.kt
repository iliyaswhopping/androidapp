package com.example.androidapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidapp.data.decks
import com.example.androidapp.ui.screens.DeckDetailScreen
import com.example.androidapp.ui.screens.DeckListScreen
import com.example.androidapp.ui.screens.FavouritesScreen
object Routes {
    const val DECKS = "decks"
    const val DETAIL = "deck/{deckId}"
    const val FAVOURITES = "favourites"

    fun detail(deckId: Int) = "deck/$deckId"
}
@Composable
fun FlashDeckApp() {
    val navController = rememberNavController()
    var favouriteIds by remember { mutableStateOf(setOf<Int>()) }

    NavHost(navController = navController, startDestination = Routes.DECKS) {
        composable(Routes.DECKS) {
            DeckListScreen(
                decks = decks,
                favouriteIds = favouriteIds,
                onDeckClick = { deck -> navController.navigate(Routes.detail(deck.id)) },
                onFavouritesClick = { navController.navigate(Routes.FAVOURITES) }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getInt("deckId")
            val deck = decks.find { it.id == deckId }
            if (deck != null) {
                DeckDetailScreen(
                    deck = deck,
                    isFavourite = deck.id in favouriteIds,
                    onFavouriteClick = {
                        favouriteIds = if (deck.id in favouriteIds) {
                            favouriteIds - deck.id
                        } else {
                            favouriteIds + deck.id
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable(Routes.FAVOURITES) {
            FavouritesScreen(
                favouriteDecks = decks.filter { it.id in favouriteIds },
                onDeckClick = { deck -> navController.navigate(Routes.detail(deck.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
