package com.wemaka.charactersrm.presetation.character

sealed class CharacterEvent {
    data class LoadCharacter(val id: Int): CharacterEvent()
}