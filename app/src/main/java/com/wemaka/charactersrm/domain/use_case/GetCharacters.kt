package com.wemaka.charactersrm.domain.use_case

import com.wemaka.charactersrm.domain.model.CharacterList
import com.wemaka.charactersrm.domain.repository.RAMRepository
import com.wemaka.charactersrm.domain.util.CharacterFilters
import com.wemaka.charactersrm.domain.util.Resource
import javax.inject.Inject

class GetCharacters @Inject constructor(
    val repository: RAMRepository
) {
    suspend operator fun invoke(
        page: Int,
        filters: CharacterFilters
    ): Resource<CharacterList> {
        return repository.getCharacterList(page, filters)
    }
}