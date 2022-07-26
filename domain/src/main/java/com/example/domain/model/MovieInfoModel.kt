package com.example.domain.model

data class MovieInfoModel(
    val title: String,
    val link: String,
    val image: String,
    val subtitle: String,
    val pubDate: String,
    val director: String,
    val actor: String,
    val userRating: Float
) {
    val rating = userRating / 2
}

