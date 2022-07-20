package com.example.moviesearchapp.data.repository.datasource

import com.example.moviesearchapp.data.model.MovieResponse
import retrofit2.Response

interface RemoteDataSource {
    suspend fun requestSearchMovie(
        query: String,
        start: Int,
        display: Int
    ): Response<MovieResponse>
}