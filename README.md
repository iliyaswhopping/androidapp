# FlashDeck

An alternative to Quizlet focused on fast memorizing, especially for language learning.

- Browse decks of flash cards loaded from an API
- Search decks by text and filter by category
- Flip-card study (tap a card to see the answer)
- Mark decks as favourites
- Loading, empty and error states with a Retry button

## How to run

1. Push `api/decks.json` to the `main` branch of this GitHub repository.
2. In `data/remote/RetrofitClient.kt` set `BASE_URL` to
   `https://raw.githubusercontent.com/<github-username>/<repo-name>/main/`
   (check it: open `BASE_URL + "api/decks.json"` in the browser, you should see the JSON).
3. Run the app. To test the error state: turn off Wi-Fi / mobile data and press Retry.

No internet at all? In `di/AppContainer.kt` use `FakeDeckRepository()` instead of `DeckRepositoryImpl(...)`.
Nothing else changes, because ViewModels only know the `DeckRepository` interface.

## Architecture

Full diagram, UiState fields and events: [`design/architecture.md`](design/architecture.md).

```
app/src/main/java/com/example/androidapp/
├── MainActivity.kt                  lifecycle logs, setContent { FlashDeckApp() }
├── di/AppContainer.kt               creates one DeckRepository for the whole app
├── domain/                          pure Kotlin, no Android, no Retrofit
│   ├── model/Deck.kt                Deck, Flashcard
│   └── repository/DeckRepository.kt  suspend fun getDecks(): List<Deck>, getDeck(id), favourites
├── data/
│   ├── remote/                      FlashDeckApi (Retrofit), RetrofitClient (BASE_URL), DeckDto
│   ├── repository/                  DeckRepositoryImpl (API -> Deck), FakeDeckRepository (delay + shouldFail)
│   └── fake/SampleDecks.kt          offline data for previews and tests
└── presentation/
    ├── decklist/                    DeckListUiState, DeckListViewModel, DeckListRoute + DeckListScreen
    ├── detail/                      DeckDetailViewModel (deckId from SavedStateHandle), DeckDetailScreen
    ├── favourites/                  FavouritesViewModel, FavouritesScreen
    ├── components/                  DeckCard, FlashcardItem, LoadingState, ErrorState, EmptyState, ...
    ├── navigation/FlashDeckApp.kt   NavHost with 3 routes
    └── theme/                       colors, typography, spacing
```

### Data flow

- **Route** (`DeckListRoute`) gets the ViewModel with `viewModel(factory = DeckListViewModel.Factory)`,
  reads `uiState` with `collectAsStateWithLifecycle()` and passes state + lambdas to the screen.
- **Screen** (`DeckListScreen`) is stateless: it gets `uiState` and lambdas and uses `when (uiState.loadState)`:
  `Loading` → spinner, `Success` → list (or empty state), `Error` → message + Retry.
- **ViewModel** keeps a private `MutableStateFlow` and exposes a read-only `StateFlow`.
  State starts as `Loading`; in `viewModelScope.launch { try { ... } catch { ... } }` it sets `Success(decks)` or `Error(message)`.
  Search and filtering happen in `showFilteredDecks()`, not in composables.
- **Repository**: `DeckRepositoryImpl.getDecks()` calls `FlashDeckApi.getDecks()` and maps `DeckDto` → `Deck`.

### Trace one click

Tap the "Science" chip → `CategoryChip.onClick` → `onCategorySelected("Science")` (lambda from `DeckListRoute`) →
`DeckListViewModel.onCategorySelected()` → `_uiState.update { it.copy(selectedCategory = "Science") }` →
`showFilteredDecks()` sets `loadState = Success(science decks)` → `StateFlow` emits →
`collectAsStateWithLifecycle()` triggers recomposition → `DeckListScreen` shows only Science decks.

### Rotation proof

Logcat filter: `tag:MainActivity | tag:DeckListViewModel`

<!-- TODO: paste your real Logcat output here after rotating the phone -->
```
D/DeckListViewModel: init
D/MainActivity: onCreate
D/MainActivity: onStart
D/MainActivity: onResume
--- rotate ---
D/MainActivity: onPause
D/MainActivity: onStop
D/MainActivity: onDestroy
D/MainActivity: onCreate
D/MainActivity: onStart
D/MainActivity: onResume
```

When the phone rotates, the configuration changes, so Android destroys the Activity and creates a new one
(onPause → onStop → onDestroy → onCreate ...). The ViewModel is not part of the Activity: it is stored in the
ViewModelStore, which Android keeps during a configuration change. The new Activity gets the same
DeckListViewModel back, so there is no second `init` and no `onCleared`, and the search text, the category and
the loaded list are still there. `onCleared` is called only when the screen is really closed (back pressed or app finished).

## Tests

`app/src/test/.../DeckListViewModelTest.kt` tests the ViewModel with `FakeDeckRepository`
(category filter, search, error state). Run: `./gradlew test`.
