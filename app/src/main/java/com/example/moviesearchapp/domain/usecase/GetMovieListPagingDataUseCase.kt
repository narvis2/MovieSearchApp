package com.example.moviesearchapp.domain.usecase

import androidx.paging.PagingData
import com.example.moviesearchapp.domain.model.MovieInfoModel
import com.example.moviesearchapp.domain.repository.NaverRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetMovieListPagingDataUseCase @Inject constructor(
    private val naverRepository: NaverRepository
) {
    operator fun invoke(
        query: StateFlow<String>
    ): Flow<PagingData<MovieInfoModel>> {
        return naverRepository.getMovieList(query)
    }
}