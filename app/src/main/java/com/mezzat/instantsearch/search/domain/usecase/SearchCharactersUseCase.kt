package com.mezzat.instantsearch.search.domain.usecase

import com.mezzat.instantsearch.search.domain.model.Character
import com.mezzat.instantsearch.search.domain.repository.SearchCharactersRepository
import com.shared.core.bases.base_usecase.BaseUseCase
import com.shared.core.state.State
import kotlinx.coroutines.flow.Flow

class SearchCharactersUseCase(
    private val repository: SearchCharactersRepository
) : BaseUseCase<String, Flow<State<List<Character>>>> {

    override fun execute(input: String): Flow<State<List<Character>>> {
        return repository.searchCharacters(input)
    }

    suspend operator fun invoke(query: String): Flow<State<List<Character>>> {
        return execute(query)
    }
}
