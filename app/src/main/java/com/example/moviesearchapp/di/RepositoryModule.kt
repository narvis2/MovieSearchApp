package com.example.moviesearchapp.di

import com.example.data.repository.NaverRepositoryImpl
import com.example.data.repository.datasource.RemoteDataSource
import com.example.domain.repository.NaverRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNaverRepository(
        remoteDataSource: RemoteDataSource
    ): NaverRepository {
        return NaverRepositoryImpl(remoteDataSource)
    }
}