package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.repository.datasource.RemoteDataSource
import com.example.data.repository.datasourceImpl.MovieInfoListPagingDataSource
import com.example.domain.model.MovieInfoModel
import com.example.domain.repository.NaverRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NaverRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : NaverRepository {
    override fun getMovieList(query: StateFlow<String>): Flow<PagingData<MovieInfoModel>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                MovieInfoListPagingDataSource(
                    remoteDataSource = remoteDataSource,
                    searchQuery = query,
                    limit = 10
                )
            }
        ).flow.distinctUntilChanged().flowOn(Dispatchers.IO)
    }
}