package com.mezzat.instantsearch.search.presentation.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mezzat.instantsearch.search.domain.model.Character
import com.mezzat.instantsearch.search.presentation.model.SearchIntent
import com.mezzat.instantsearch.search.presentation.model.SearchUiState
import com.mezzat.instantsearch.search.presentation.viewmodel.SearchViewModel
import com.mezzat.instantsearch.ui.theme.InstantSearchTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreenContent(
        searchQuery = searchQuery,
        uiState = uiState,
        onQueryChanged = viewModel::onQueryChanged,
        onClearQuery = { viewModel.sendIntent(SearchIntent.ClearQuery) }
    )
}

// Mock Data for Previews
private val mockCharacters = listOf(
    Character(
        id = 1,
        name = "Rick Sanchez",
        species = "Human",
        status = "Alive",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
    ),
    Character(
        id = 2,
        name = "Morty Smith",
        species = "Human",
        status = "Alive",
        image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg"
    ),
    Character(
        id = 3,
        name = "Summer Smith",
        species = "Human",
        status = "Alive",
        image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg"
    )
)

// Previews for Idle State
@Preview(name = "Idle - Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun SearchScreenIdlePreviewLight() {
    InstantSearchTheme(darkTheme = false) {
        SearchScreenContent(
            searchQuery = "",
            uiState = SearchUiState.Idle,
            onQueryChanged = {},
            onClearQuery = {}
        )
    }
}

@Preview(name = "Idle - Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SearchScreenIdlePreviewDark() {
    InstantSearchTheme(darkTheme = true) {
        SearchScreenContent(
            searchQuery = "",
            uiState = SearchUiState.Idle,
            onQueryChanged = {},
            onClearQuery = {}
        )
    }
}

// Previews for Loading State
@Preview(name = "Loading - Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun SearchScreenLoadingPreviewLight() {
    InstantSearchTheme(darkTheme = false) {
        SearchScreenContent(
            searchQuery = "Rick",
            uiState = SearchUiState.Loading,
            onQueryChanged = {},
            onClearQuery = {}
        )
    }
}

@Preview(name = "Loading - Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SearchScreenLoadingPreviewDark() {
    InstantSearchTheme(darkTheme = true) {
        SearchScreenContent(
            searchQuery = "Rick",
            uiState = SearchUiState.Loading,
            onQueryChanged = {},
            onClearQuery = {}
        )
    }
}

// Previews for Success State
@Preview(name = "Success - Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun SearchScreenSuccessPreviewLight() {
    InstantSearchTheme(darkTheme = false) {
        SearchScreenContent(
            searchQuery = "Rick",
            uiState = SearchUiState.Success(mockCharacters),
            onQueryChanged = {},
            onClearQuery = {}
        )
    }
}

@Preview(name = "Success - Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SearchScreenSuccessPreviewDark() {
    InstantSearchTheme(darkTheme = true) {
        SearchScreenContent(
            searchQuery = "Rick",
            uiState = SearchUiState.Success(mockCharacters),
            onQueryChanged = {},
            onClearQuery = {}
        )
    }
}

// Previews for Empty State
@Preview(name = "Empty - Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun SearchScreenEmptyPreviewLight() {
    InstantSearchTheme(darkTheme = false) {
        SearchScreenContent(
            searchQuery = "UnknownCharacter",
            uiState = SearchUiState.Empty,
            onQueryChanged = {},
            onClearQuery = {}
        )
    }
}

@Preview(name = "Empty - Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SearchScreenEmptyPreviewDark() {
    InstantSearchTheme(darkTheme = true) {
        SearchScreenContent(
            searchQuery = "UnknownCharacter",
            uiState = SearchUiState.Empty,
            onQueryChanged = {},
            onClearQuery = {}
        )
    }
}

// Previews for Error State
@Preview(name = "Error - Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun SearchScreenErrorPreviewLight() {
    InstantSearchTheme(darkTheme = false) {
        SearchScreenContent(
            searchQuery = "Rick",
            uiState = SearchUiState.Error("No internet connection. Please check your network."),
            onQueryChanged = {},
            onClearQuery = {}
        )
    }
}

@Preview(name = "Error - Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SearchScreenErrorPreviewDark() {
    InstantSearchTheme(darkTheme = true) {
        SearchScreenContent(
            searchQuery = "Rick",
            uiState = SearchUiState.Error("No internet connection. Please check your network."),
            onQueryChanged = {},
            onClearQuery = {}
        )
    }
}
