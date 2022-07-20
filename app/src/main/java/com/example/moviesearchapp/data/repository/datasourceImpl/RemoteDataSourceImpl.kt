package com.example.moviesearchapp.data.repository.datasourceImpl

import com.example.moviesearchapp.data.api.NaverApiService
import com.example.moviesearchapp.data.model.MovieResponse
import com.example.moviesearchapp.data.repository.datasource.RemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val naverApiService: NaverApiService
) : RemoteDataSource {

    override suspend fun requestSearchMovie(
        query: String,
        start: Int,
        display: Int
    ): Response<MovieResponse> {
        return naverApiService.searchMovie(query, start, display)
    }
}