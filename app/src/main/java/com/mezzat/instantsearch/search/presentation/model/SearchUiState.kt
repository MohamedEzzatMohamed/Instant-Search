package com.mezzat.instantsearch.search.presentation.model

import com.mezzat.instantsearch.search.domain.model.Character
import com.shared.core.bases.base_viewmodel.ViewEvent
import com.shared.core.bases.base_viewmodel.ViewIntent
import com.shared.core.bases.base_viewmodel.ViewState

sealed interface SearchUiState : ViewState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Success(val characters: List<Character>) : SearchUiState
    data object Empty : SearchUiState
    data class Error(val message: String) : SearchUiState
}

sealed interface SearchIntent : ViewIntent {
    data class QueryChanged(val query: String) : SearchIntent
    data object ClearQuery : SearchIntent
}

sealed interface SearchEvent : ViewEvent
