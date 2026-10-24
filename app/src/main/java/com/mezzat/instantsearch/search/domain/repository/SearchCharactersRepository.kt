package com.mezzat.instantsearch.search.domain.repository

import com.mezzat.instantsearch.search.domain.model.Character
import com.shared.core.state.State
import kotlinx.coroutines.flow.Flow

interface SearchCharactersRepository {
    fun searchCharacters(query: String): Flow<State<List<Character>>>
}
