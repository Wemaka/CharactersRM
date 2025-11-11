package com.wemaka.charactersrm.data.repository

import com.wemaka.charactersrm.data.data_source.NetworkChecker
import com.wemaka.charactersrm.data.data_source.RAMApi
import com.wemaka.charactersrm.domain.model.Character
import com.wemaka.charactersrm.domain.model.CharacterItem
import com.wemaka.charactersrm.domain.model.CharacterList
import com.wemaka.charactersrm.domain.model.Info
import com.wemaka.charactersrm.domain.repository.RAMRepository
import com.wemaka.charactersrm.domain.util.CharacterFilters
import com.wemaka.charactersrm.domain.util.Resource
import timber.log.Timber
import javax.inject.Inject

class RAMRepositoryImpl @Inject constructor(
    val api: RAMApi,
    val networkChecker: NetworkChecker
) : RAMRepository {
    private var cachedCharacters: CharacterList? = null

    override suspend fun getCharacterList(page: Int, filters: CharacterFilters): Resource<CharacterList> {
        return try {
            if (networkChecker.hasInternetConnection()) {
                var characterList = api.getCharacterList(
                    page,
                    filters.name,
                    filters.status?.toString()?.lowercase() ?: "",
                    filters.species,
                    filters.type,
                    filters.gender?.toString()?.lowercase() ?: ""
                )

                cachedCharacters = if (page == 1) {
                    characterList
                } else {
                    characterList.copy(
                        characters = (cachedCharacters?.characters ?: emptyList()) + characterList.characters
                    )
                }

                Resource.Success(cachedCharacters)
            } else {
                cachedCharacters?.let {
                    Resource.Success(it)
                    Resource.Success(
                        it.copy(
                            characters = applyFilters(it.characters, filters)
                        )
                    )
                } ?: Resource.Error("An unknown error occurred.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun getCharacterInfo(characterId: Int): Resource<Character> {
        return try {
            Resource.Success(api.getCharacterInfo(characterId))
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    private fun applyFilters(list: List<CharacterItem>, filters: CharacterFilters): List<CharacterItem> {
        return list.filter { c ->
            (filters.name.isBlank() || c.name.contains(filters.name, ignoreCase = true)) &&
                    (filters.species.isBlank() || c.species.equals(filters.species, ignoreCase = true)) &&
                    (filters.status == null || c.status.equals(filters.status.toString(), ignoreCase = true)) &&
                    (filters.gender == null || c.gender.equals(filters.gender.toString(), ignoreCase = true))
        }
    }
}