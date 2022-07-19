package com.example.moviesearchapp.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverApiService {

    @GET("/search/movie")
    suspend fun searchMovie(
        @Query("query") query: String
    ): Response<>
}