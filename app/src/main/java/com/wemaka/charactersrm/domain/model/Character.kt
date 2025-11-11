package com.wemaka.charactersrm.domain.model

data class Character(
    val id: Int,
    val name: String,
    val gender: String,
    val image: String,
    val status: String,
    val created: String,
    val episode: List<String>,
    val location: Location,
    val origin: Origin,
    val species: String,
    val type: String,
    val url: String
)