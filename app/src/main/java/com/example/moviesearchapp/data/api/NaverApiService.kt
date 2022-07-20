package com.example.moviesearchapp.data.api

import com.example.moviesearchapp.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverApiService {

    @GET("/v1/search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("start") start: Int,
        @Query("display") display: Int
    ): Response<MovieResponse>
}