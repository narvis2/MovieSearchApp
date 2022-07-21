package com.example.data.mapper

import com.example.data.model.MovieInfo
import com.example.domain.model.MovieInfoModel

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
            userRating= it.userRating.toFloat()
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
        userRating= this.userRating.toFloat()
    )
}