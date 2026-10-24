package com.mezzat.instantsearch.search.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponseDto(
    val results: List<CharacterDto> = emptyList()
)
