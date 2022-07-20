package com.example.moviesearchapp.data.mapper

import com.example.moviesearchapp.data.model.MovieInfo
import com.example.moviesearchapp.domain.model.MovieInfoModel

object ObjectMapper {
    fun List<MovieInfo>.toMovieInfoListModel(): List<MovieInfoModel> = map {
        MovieInfoModel(
            title = it.title,
            link= it.link,
            image= it.image,
            subtitle= it.subtitle,
            pubDate= it.pubDate,
            director= it.director,
            actor= it.actor,
            userRating= it.userRating
        )
    }

    fun MovieInfo.toMovieInfoModel(): MovieInfoModel = MovieInfoModel(
        title = this.title,
        link= this.link,
        image= this.image,
        subtitle= this.subtitle,
        pubDate= this.pubDate,
        director= this.director,
        actor= this.actor,
        userRating= this.userRating
    )
}