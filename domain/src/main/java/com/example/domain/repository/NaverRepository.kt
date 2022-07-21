package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.MovieInfoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NaverRepository {
    fun getMovieList(query: StateFlow<String>): Flow<PagingData<MovieInfoModel>>
}