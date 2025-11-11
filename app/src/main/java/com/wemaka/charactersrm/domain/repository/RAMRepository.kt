package com.wemaka.charactersrm.domain.repository

import com.wemaka.charactersrm.domain.model.Character
import com.wemaka.charactersrm.domain.model.CharacterList
import com.wemaka.charactersrm.domain.util.CharacterFilters
import com.wemaka.charactersrm.domain.util.Resource

interface RAMRepository {
    suspend fun getCharacterList(
        page: Int,
        filters: CharacterFilters
    ): Resource<CharacterList>

    suspend fun getCharacterInfo(characterId: Int): Resource<Character>
}