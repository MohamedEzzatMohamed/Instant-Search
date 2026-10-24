package com.mezzat.instantsearch.search.data.repository

import com.mezzat.instantsearch.search.data.model.CharacterDto
import com.mezzat.instantsearch.search.data.model.CharacterResponseDto
import com.mezzat.instantsearch.search.domain.model.Character
import com.mezzat.instantsearch.search.domain.repository.SearchCharactersRepository
import com.shared.core.network.ApiClient
import com.shared.core.network.NetworkResult
import com.shared.core.network.safeApiCall
import com.shared.core.state.State
import kotlinx.coroutines.flow.Flow

class SearchCharactersRepositoryImpl(
    private val apiClient: ApiClient
) : SearchCharactersRepository {

    override fun searchCharacters(query: String): Flow<State<List<Character>>> {
        return safeApiCall(
            apiCall = {
                val response = apiClient.get<CharacterResponseDto>("character/?name=$query")
                if (response is NetworkResult.Failure && response.code == 404) {
                    NetworkResult.Success(CharacterResponseDto(emptyList()), 200)
                } else {
                    response
                }
            },
            mapper = { dto ->
                dto.results.map { it.toDomain() }
            }
        )
    }

    private fun CharacterDto.toDomain(): Character {
        return Character(
            id = id,
            name = name,
            species = species,
            status = status,
            image = image
        )
    }
}
