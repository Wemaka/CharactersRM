package com.wemaka.charactersrm.data.data_source

import com.wemaka.charactersrm.domain.model.Character
import com.wemaka.charactersrm.domain.model.CharacterList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RAMApi {
    @GET("character")
    suspend fun getCharacterList(
        @Query("page") page: Int = 1,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("type") type: String,
        @Query("gender") gender: String
    ): CharacterList

    @GET("character/{id}")
    suspend fun getCharacterInfo(
        @Path("id") id: Int
    ): Character
}