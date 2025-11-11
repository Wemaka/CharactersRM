package com.wemaka.charactersrm.presetation.characters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wemaka.charactersrm.core.util.Constants.PAGE_SIZE
import com.wemaka.charactersrm.data.data_source.NetworkChecker
import com.wemaka.charactersrm.domain.model.CharacterItem
import com.wemaka.charactersrm.domain.use_case.GetCharacters
import com.wemaka.charactersrm.domain.util.CharacterFilters
import com.wemaka.charactersrm.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharacters: GetCharacters,
    private val networkChecker: NetworkChecker
) : ViewModel() {

    private var curPage = 1

    private val filtersFlow = MutableSharedFlow<CharacterFilters>(
        replay = 0,
        extraBufferCapacity = 1
    )

    private var cachedCharacterList = listOf<CharacterItem>()

    private var isSearchStarting = true

    var state by mutableStateOf(CharactersState())
        private set

    init {
        viewModelScope.launch {
            filtersFlow
                .distinctUntilChanged()
                .debounce(200)
                .collect { filters ->
                    curPage = 1

                    state = state.copy(
//                        charactersList = emptyList(),
                        charactersFilters = filters
                    )

                    loadCharacters()
                }
        }

        loadCharacters()
    }

    fun onEvent(event: CharactersEvent) {
        when (event) {
            is CharactersEvent.LoadCharacters -> loadCharacters()
            is CharactersEvent.RefreshLoadCharacters -> {
                curPage = 1
                state = state.copy(
                    charactersList = emptyList()
                )

                loadCharacters()
            }

            is CharactersEvent.Filters -> {
                filtersFlow.tryEmit(event.filters)
            }

            is CharactersEvent.ToggleFiltersSection -> {
                state = state.copy(
                    isFiltersSectionVisible = !state.isFiltersSectionVisible
                )
            }
        }
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = getCharacters(
                page = curPage,
                filters = state.charactersFilters
            )

            when (result) {
                is Resource.Success -> {
                    state = state.copy(
                        isEndReached = curPage * PAGE_SIZE >= result.data!!.info.count
                    )

                    val newCharacters = result.data.characters

                    if (networkChecker.hasInternetConnection()) {
                        curPage++
                    }

                    state = state.copy(
                        charactersList = newCharacters,
                        isLoading = false,
                        loadError = ""
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        loadError = "Sorry, nothing was found"
                    )
                }
            }
        }
    }
}