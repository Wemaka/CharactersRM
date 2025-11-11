package com.wemaka.charactersrm.di

import com.wemaka.charactersrm.data.repository.RAMRepositoryImpl
import com.wemaka.charactersrm.domain.repository.RAMRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRAMRepository(
        ramRepositoryImpl: RAMRepositoryImpl
    ): RAMRepository
}