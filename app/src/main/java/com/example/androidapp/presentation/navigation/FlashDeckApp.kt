package com.example.androidapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidapp.presentation.decklist.DeckListRoute
import com.example.androidapp.presentation.detail.DeckDetailRoute
import com.example.androidapp.presentation.favourites.FavouritesRoute

object Routes {
    const val DECKS = "decks"
    const val DETAIL = "deck/{deckId}"
    const val FAVOURITES = "favourites"

    fun detail(deckId: Int) = "deck/$deckId"
}

// Only navigation here. No data and no state: each Route gets its own ViewModel.
@Composable
fun FlashDeckApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.DECKS) {
        composable(Routes.DECKS) {
            DeckListRoute(
                onDeckClick = { deck -> navController.navigate(Routes.detail(deck.id)) },
                onFavouritesClick = { navController.navigate(Routes.FAVOURITES) }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
        ) {
            // deckId goes to DeckDetailViewModel through SavedStateHandle.
            DeckDetailRoute(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.FAVOURITES) {
            FavouritesRoute(
                onDeckClick = { deck -> navController.navigate(Routes.detail(deck.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
