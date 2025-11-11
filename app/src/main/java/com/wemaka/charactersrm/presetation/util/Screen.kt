package com.wemaka.charactersrm.presetation.util

sealed class Screen(val route: String) {
    object CharactersScreen: Screen("characters_screen")
    object CharacterScreen: Screen("character_screen")
}