package com.wemaka.charactersrm.domain.model

data class CharacterItem(
    val id: Int,
    val name: String,
    val gender: String,
    val image: String,
    val status: String,
    val species: String,
    val url: String
)
