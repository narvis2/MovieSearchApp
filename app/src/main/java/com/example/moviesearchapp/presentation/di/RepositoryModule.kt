package com.example.moviesearchapp.presentation.di

import com.example.moviesearchapp.domain.repository.NaverRepository
import com.example.moviesearchapp.data.repository.NaverRepositoryImpl
import com.example.moviesearchapp.data.repository.datasource.RemoteDataSource
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