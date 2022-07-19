package com.example.moviesearchapp.data.model

data class MovieResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<MovieInfo>
)
