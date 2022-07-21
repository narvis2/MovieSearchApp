package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.domain.model.MovieInfoModel
import com.example.domain.repository.NaverRepository
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