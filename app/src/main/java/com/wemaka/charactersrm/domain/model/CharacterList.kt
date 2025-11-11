package com.wemaka.charactersrm.domain.model

import com.squareup.moshi.Json

data class CharacterList(
    val info: Info,
    @field:Json(name = "results")
    val characters: List<CharacterItem>
)