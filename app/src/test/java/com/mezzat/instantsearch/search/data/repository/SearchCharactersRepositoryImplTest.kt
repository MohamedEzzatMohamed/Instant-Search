package com.mezzat.instantsearch.search.data.repository

import com.mezzat.instantsearch.search.domain.model.Character
import com.shared.core.network.ApiClient
import com.shared.core.state.State
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchCharactersRepositoryImplTest {

    private fun createTestApiClient(mockEngine: MockEngine): ApiClient {
        val client = HttpClient(mockEngine) {
            defaultRequest {
                url("https://rickandmortyapi.com/api/")
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }
        }
        return ApiClient(client)
    }

    @Test
    fun searchCharacters_returnsSuccessStateWithMappedCharacters() = runBlocking {
        val mockJson = """
            {
              "results": [
                {
                  "id": 1,
                  "name": "Rick Sanchez",
                  "species": "Human",
                  "status": "Alive",
                  "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                }
              ]
            }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = mockJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val apiClient = createTestApiClient(mockEngine)
        val repository = SearchCharactersRepositoryImpl(apiClient)

        val states = repository.searchCharacters("Rick").toList()

        val successState = states.filterIsInstance<State.Success<List<Character>>>().firstOrNull()
        assertTrue("Expected Success state, but got: $states", successState != null)
        val characters = successState?.data
        assertEquals(1, characters?.size)
        assertEquals("Rick Sanchez", characters?.first()?.name)
        assertEquals("Human", characters?.first()?.species)
        assertEquals("Alive", characters?.first()?.status)
    }

    @Test
    fun searchCharacters_returnsSuccessStateWithEmptyListOn404() = runBlocking {
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"error": "There is nothing here"}""",
                status = HttpStatusCode.NotFound,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val apiClient = createTestApiClient(mockEngine)
        val repository = SearchCharactersRepositoryImpl(apiClient)

        val states = repository.searchCharacters("Unknown").toList()

        val successState = states.filterIsInstance<State.Success<List<Character>>>().firstOrNull()
        assertTrue("Expected Success state on 404, but got: $states", successState != null)
        val characters = successState?.data
        assertTrue(characters?.isEmpty() == true)
    }
}
