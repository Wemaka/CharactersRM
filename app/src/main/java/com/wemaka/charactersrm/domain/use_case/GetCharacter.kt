package com.wemaka.charactersrm.domain.use_case

import com.wemaka.charactersrm.domain.model.Character
import com.wemaka.charactersrm.domain.repository.RAMRepository
import com.wemaka.charactersrm.domain.util.Resource
import javax.inject.Inject

class GetCharacter @Inject constructor(
    val repository: RAMRepository
) {
    suspend operator fun invoke(characterId: Int): Resource<Character> {
        // if no internet
        // return repository.getLocalCharacterInfo
        return repository.getCharacterInfo(characterId)
    }
}