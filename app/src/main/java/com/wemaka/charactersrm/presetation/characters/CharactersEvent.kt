package com.wemaka.charactersrm.presetation.characters

import com.wemaka.charactersrm.domain.util.CharacterFilters

sealed class CharactersEvent {
    object LoadCharacters: CharactersEvent()
    object RefreshLoadCharacters: CharactersEvent()
    data class Filters(val filters: CharacterFilters): CharactersEvent()
    object ToggleFiltersSection: CharactersEvent()
}