package com.example.data.repository.datasource

import com.example.data.model.MovieResponse
import retrofit2.Response

interface RemoteDataSource {
    suspend fun requestSearchMovie(
        query: String,
        start: Int,
        display: Int
    ): Response<MovieResponse>
}