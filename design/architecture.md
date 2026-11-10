# FlashDeck architecture (MVVM + domain + Retrofit)

## The restaurant

```
 VIEW (waiter)                 VIEWMODEL (chef)               REPOSITORY (fridge)
 ─────────────                 ────────────────               ───────────────────
 DeckListScreen   ──events──▶  DeckListViewModel   ─────────▶  DeckRepository (interface, domain)
 DeckDetailScreen ──events──▶  DeckDetailViewModel ─────────▶     └─ DeckRepositoryImpl (data)
 FavouritesScreen ──events──▶  FavouritesViewModel ─────────▶          └─ FlashDeckApi (Retrofit)
                  ◀──state───                                              └─ api/decks.json on GitHub
                                                                   └─ FakeDeckRepository (delay + shouldFail)
```

Events go up to the ViewModel. State comes down to the screen.

## Layers and folders

| Layer | Folder | Files |
|---|---|---|
| Domain (no Android, no Retrofit) | `domain/model`, `domain/repository` | `Deck`, `Flashcard`, `DeckRepository` |
| Data: API | `data/remote` | `FlashDeckApi`, `RetrofitClient`, `DeckDto` |
| Data: repository | `data/repository` | `DeckRepositoryImpl`, `FakeDeckRepository` |
| Presentation | `presentation/...` | ViewModels, UiState, Route + Screen composables |

## Order board (UiState fields)

**DeckListUiState** (data class, only `val`)
- `loadState`: `Loading` | `Success(decks)` | `Error(message)` (empty = `Success(emptyList())`)
- `searchQuery: String`
- `selectedCategory: String`
- `categories: List<String>`
- `favouriteIds: Set<Int>`

**DeckDetailUiState**: `Loading` | `Success(deck, isFavourite)` | `Error(message)`

**FavouritesUiState**: `Loading` | `Success(decks)` | `Error(message)`

## Events

| Screen | Event | ViewModel function |
|---|---|---|
| Deck list | type in search field | `onSearchChange(query)` |
| Deck list | tap category chip | `onCategorySelected(category)` |
| Deck list / Detail / Favourites | tap Retry | `onRetry()` |
| Detail | tap heart | `onFavouriteClick()` |

## Trace one click: tapping the "Science" chip

1. `CategoryChip` in `DeckListScreen.kt` calls `onCategorySelected("Science")`.
2. `DeckListRoute` passed `viewModel::onCategorySelected`, so `DeckListViewModel.onCategorySelected("Science")` runs.
3. It does `_uiState.update { it.copy(selectedCategory = "Science") }` and then `showFilteredDecks()`,
   which filters `allDecks` and sets `loadState = Success(only Science decks)`.
4. `uiState` is a `StateFlow`; `DeckListRoute` collects it with `collectAsStateWithLifecycle()`.
5. Compose recomposes `DeckListScreen` with the new state: the chip is selected and the list shows only Science decks.

## Trace loading data (SIS5)

`DeckListViewModel.init` → `loadDecks()` → state `Loading` (spinner) →
`viewModelScope.launch { try { repository.getDecks() } }` → `DeckRepositoryImpl.getDecks()` →
`FlashDeckApi.getDecks()` (Retrofit GET `api/decks.json`) → `List<DeckDto>` → `toDeck()` → `List<Deck>` →
ViewModel sets `Success(decks)` (list). If there is no internet, Retrofit throws `IOException` →
`catch` sets `Error("No internet connection...")` → screen shows the message and Retry.
