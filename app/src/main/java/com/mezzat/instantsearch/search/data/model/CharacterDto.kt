package com.mezzat.instantsearch.search.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val image: String
)
