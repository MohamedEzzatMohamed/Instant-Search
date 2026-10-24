package com.mezzat.instantsearch.search.domain.usecase

import com.mezzat.instantsearch.search.domain.model.Character
import com.mezzat.instantsearch.search.domain.repository.SearchCharactersRepository
import com.shared.core.app_error.AppError
import com.shared.core.state.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchCharactersUseCaseTest {

    @Test
    fun execute_callsRepository_andReturnsFlowOfState() = runBlocking {
        val expectedCharacters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                species = "Human",
                status = "Alive",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
            )
        )
        val fakeRepository = object : SearchCharactersRepository {
            override fun searchCharacters(query: String): Flow<State<List<Character>>> {
                return flowOf(
                    State.Loading(),
                    State.Success(expectedCharacters)
                )
            }
        }
        val useCase = SearchCharactersUseCase(fakeRepository)

        val states = useCase.execute("Rick").toList()

        assertTrue(states[0] is State.Loading)
        assertTrue(states[1] is State.Success)
        assertEquals(expectedCharacters, (states[1] as State.Success<List<Character>>).data)
    }

    @Test
    fun execute_returnsErrorStateOnRepositoryFailure() = runBlocking {
        val fakeRepository = object : SearchCharactersRepository {
            override fun searchCharacters(query: String): Flow<State<List<Character>>> {
                return flowOf(
                    State.Loading(),
                    State.Error(AppError.NoInternet)
                )
            }
        }
        val useCase = SearchCharactersUseCase(fakeRepository)

        val states = useCase.execute("Rick").toList()

        assertTrue(states[0] is State.Loading)
        assertTrue(states[1] is State.Error)
        assertEquals(AppError.NoInternet, (states[1] as State.Error<List<Character>>).error)
    }
}
