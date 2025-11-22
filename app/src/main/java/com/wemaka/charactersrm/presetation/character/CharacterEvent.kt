package com.wemaka.charactersrm.presetation.character

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color

sealed class CharacterEvent {
    data class LoadCharacter(val id: Int) : CharacterEvent()
    data class CalcDominantColor(val drawable: Drawable, val onFinish: (Color) -> Unit) : CharacterEvent()
}