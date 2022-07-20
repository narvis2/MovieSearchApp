package com.example.moviesearchapp.presentation.di

import com.example.moviesearchapp.domain.usecase.GetMovieListPagingDataUseCase
import com.example.moviesearchapp.domain.repository.NaverRepository
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