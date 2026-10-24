package com.mezzat.instantsearch.search.presentation.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
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

@Composable
fun SearchScreenContent(
    searchQuery: String,
    uiState: SearchUiState,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Surface(
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Live Autocomplete Search",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onQueryChanged,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search Rick & Morty characters...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon"
                            )
                        },
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = searchQuery.isNotEmpty(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(onClick = onClearQuery) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search"
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is SearchUiState.Idle -> {
                    InfoMessage(
                        icon = Icons.Default.Search,
                        message = "Type a character name to start searching..."
                    )
                }
                is SearchUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                is SearchUiState.Success -> {
                    CharacterList(characters = uiState.characters)
                }
                is SearchUiState.Empty -> {
                    InfoMessage(
                        icon = Icons.Default.Search,
                        message = if (searchQuery.isNotEmpty()) {
                            "No characters found matching \"$searchQuery\""
                        } else {
                            "No characters found."
                        }
                    )
                }
                is SearchUiState.Error -> {
                    InfoMessage(
                        icon = Icons.Default.Clear,
                        message = uiState.message,
                        isError = true
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterList(
    characters: List<Character>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = characters,
            key = { it.id }
        ) { character ->
            CharacterCard(character = character)
        }
    }
}

@Composable
fun CharacterCard(
    character: Character,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = character.image,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    }
                },
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = character.species,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                StatusChip(status = character.status)
            }
        }
    }
}

@Composable
fun StatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val statusColor = when (status.lowercase()) {
        "alive" -> Color(0xFF4CAF50)
        "dead" -> Color(0xFFF44336)
        else -> Color(0xFF9E9E9E)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = statusColor, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = status,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun InfoMessage(
    icon: ImageVector,
    message: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
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
