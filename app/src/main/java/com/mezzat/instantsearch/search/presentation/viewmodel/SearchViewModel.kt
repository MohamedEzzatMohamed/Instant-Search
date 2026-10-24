package com.mezzat.instantsearch.search.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.mezzat.instantsearch.search.domain.model.Character
import com.mezzat.instantsearch.search.domain.usecase.SearchCharactersUseCase
import com.mezzat.instantsearch.search.presentation.model.SearchEvent
import com.mezzat.instantsearch.search.presentation.model.SearchIntent
import com.mezzat.instantsearch.search.presentation.model.SearchUiState
import com.shared.core.app_error.AppError
import com.shared.core.bases.base_viewmodel.BaseViewModel
import com.shared.core.state.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModel(
    private val searchCharactersUseCase: SearchCharactersUseCase
) : BaseViewModel<SearchIntent, SearchUiState, SearchEvent>(SearchUiState.Idle) {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val uiState: StateFlow<SearchUiState> = uiStateFlow

    init {
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300.milliseconds)
                .map { it.trim() }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isEmpty()) {
                        flowOf(State.Success(emptyList()))
                    } else {
                        searchCharactersUseCase.execute(query)
                    }
                }
                .collect { state ->
                    updateStateFlow {
                        when (state) {
                            is State.Loading -> SearchUiState.Loading
                            is State.Success -> {
                                val characters = state.data.orEmpty()
                                val currentQuery = _searchQuery.value.trim()
                                if (currentQuery.isEmpty()) {
                                    SearchUiState.Idle
                                } else if (characters.isEmpty()) {
                                    SearchUiState.Empty
                                } else {
                                    SearchUiState.Success(characters)
                                }
                            }
                            is State.Error -> SearchUiState.Error(mapErrorToMessage(state.error))
                            is State.Failure -> SearchUiState.Error(state.message)
                        }
                    }
                }
        }
    }

    override fun processIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> {
                _searchQuery.value = intent.query
            }
            SearchIntent.ClearQuery -> {
                _searchQuery.value = ""
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        sendIntent(SearchIntent.QueryChanged(newQuery))
    }

    private fun mapErrorToMessage(error: AppError): String {
        return when (error) {
            AppError.NoInternet -> "No internet connection. Please check your network."
            AppError.Unauthorized -> "Unauthorized request."
            AppError.Forbidden -> "Access forbidden."
            is AppError.NotFound -> error.message.ifEmpty { "No characters found." }
            AppError.ServerError -> "Server error. Please try again later."
            is AppError.Unknown -> error.message.ifEmpty { "An unexpected error occurred." }
        }
    }
}
