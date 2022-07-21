package com.example.moviesearchapp.di

import com.example.domain.repository.NaverRepository
import com.example.domain.usecase.GetMovieListPagingDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetMovieListPagingDataUseCase(
        naverRepository: NaverRepository
    ): GetMovieListPagingDataUseCase {
        return GetMovieListPagingDataUseCase(naverRepository)
    }
}