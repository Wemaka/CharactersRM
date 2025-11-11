package com.wemaka.charactersrm.domain.util

import com.wemaka.charactersrm.core.util.CharacterStatus

data class CharacterFilters(
    val name: String = "",
    val status: CharacterStatus? = null,
    val species: String = "",
    val type: String = "",
    val gender: CharacterGender? = null
)