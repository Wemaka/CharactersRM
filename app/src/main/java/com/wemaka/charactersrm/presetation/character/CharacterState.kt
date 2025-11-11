package com.wemaka.charactersrm.presetation.character

import com.wemaka.charactersrm.domain.model.Character

data class CharacterState(
    val isLoading: Boolean = false,
    val character: Character? = null,
    val loadError: String = ""
)
