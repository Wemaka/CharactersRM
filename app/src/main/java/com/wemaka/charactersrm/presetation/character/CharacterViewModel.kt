package com.wemaka.charactersrm.presetation.character

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.wemaka.charactersrm.domain.model.Character
import com.wemaka.charactersrm.domain.model.CharacterList
import com.wemaka.charactersrm.domain.use_case.GetCharacter
import com.wemaka.charactersrm.domain.util.Resource
import com.wemaka.charactersrm.presetation.characters.CharactersEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val getCharacter: GetCharacter
) : ViewModel() {

    var state by mutableStateOf(CharacterState())
        private set

    fun onEvent(event: CharacterEvent) {
        when (event) {
            is CharacterEvent.LoadCharacter -> getCharacterDetail(event.id)
            is CharacterEvent.CalcDominantColor -> viewModelScope.launch {
                val bmp = (event.drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

                Palette.from(bmp).generate { palette ->
                    palette?.dominantSwatch?.rgb?.let { colorValue ->
                        event.onFinish(Color(colorValue))
                    }
                }
            }
        }
    }

    fun getCharacterDetail(id: Int) {
        viewModelScope.launch {
            val result = getCharacter(id)

            state = when (result) {
                is Resource.Success -> {
                    state.copy(
                        character = result.data
                    )
                }

                is Resource.Error -> {
                    state.copy(
                        loadError = "Character not found"
                    )
                }
            }
        }
    }
}