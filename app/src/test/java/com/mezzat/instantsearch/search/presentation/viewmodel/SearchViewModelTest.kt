package com.mezzat.instantsearch.search.presentation.viewmodel

import com.mezzat.instantsearch.search.domain.model.Character
import com.mezzat.instantsearch.search.domain.repository.SearchCharactersRepository
import com.mezzat.instantsearch.search.domain.usecase.SearchCharactersUseCase
import com.mezzat.instantsearch.search.presentation.model.SearchIntent
import com.mezzat.instantsearch.search.presentation.model.SearchUiState
import com.shared.core.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchQuery_initialValueIsEmpty() = runTest {
        val fakeRepository = object : SearchCharactersRepository {
            override fun searchCharacters(query: String): Flow<State<List<Character>>> {
                return flowOf(State.Success(emptyList()))
            }
        }
        val useCase = SearchCharactersUseCase(fakeRepository)
        val viewModel = SearchViewModel(useCase)

        assertEquals("", viewModel.searchQuery.value)
        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun onQueryChanged_updatesSearchQuery() = runTest {
        val fakeRepository = object : SearchCharactersRepository {
            override fun searchCharacters(query: String): Flow<State<List<Character>>> {
                return flowOf(State.Success(emptyList()))
            }
        }
        val useCase = SearchCharactersUseCase(fakeRepository)
        val viewModel = SearchViewModel(useCase)

        viewModel.onQueryChanged("Morty")
        testScheduler.advanceUntilIdle()

        assertEquals("Morty", viewModel.searchQuery.value)
    }

    @Test
    fun searchPipeline_emitsSuccessState_afterDebounceAndSearch() = runTest {
        val characters = listOf(
            Character(1, "Morty Smith", "Human", "Alive", "image.jpeg")
        )
        val fakeRepository = object : SearchCharactersRepository {
            override fun searchCharacters(query: String): Flow<State<List<Character>>> {
                return flowOf(State.Loading(), State.Success(characters))
            }
        }
        val useCase = SearchCharactersUseCase(fakeRepository)
        val viewModel = SearchViewModel(useCase)

        viewModel.sendIntent(SearchIntent.QueryChanged("Morty"))
        testScheduler.advanceUntilIdle()

        assertEquals(SearchUiState.Success(characters), viewModel.uiState.value)
    }
}
