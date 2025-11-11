package com.wemaka.charactersrm.presetation.characters

import com.wemaka.charactersrm.domain.model.CharacterItem
import com.wemaka.charactersrm.domain.util.CharacterFilters

data class CharactersState(
    val charactersList: List<CharacterItem> = emptyList(),
    val charactersFilters: CharacterFilters = CharacterFilters(),
    val isFiltersSectionVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val loadError: String = ""
)