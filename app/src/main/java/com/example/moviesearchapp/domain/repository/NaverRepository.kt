package com.example.moviesearchapp.data.model

interface NaverRepository {
    fun getMovieList(query: StateFlow<String>): Flow<PagingData<MovieInfoModel>>
}