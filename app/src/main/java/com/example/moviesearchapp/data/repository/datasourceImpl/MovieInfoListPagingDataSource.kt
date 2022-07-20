package com.example.moviesearchapp.data.repository.datasourceImpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesearchapp.data.mapper.ObjectMapper.toMovieInfoListModel
import com.example.moviesearchapp.data.model.LoadException
import com.example.moviesearchapp.data.repository.datasource.RemoteDataSource
import com.example.moviesearchapp.domain.model.MovieInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.floor

class MovieInfoListPagingDataSource @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val searchQuery: StateFlow<String>,
    private val limit: Int
) : PagingSource<Int, MovieInfoModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieInfoModel> {
        val pageNumber = params.key ?: 1

        try {
            val response = withContext(Dispatchers.IO) {
                remoteDataSource.requestSearchMovie(
                    query = searchQuery.value,
                    start = pageNumber,
                    display = limit
                )
            }

            if (response.isSuccessful) {
                response.body()?.let {
                    val movieList = it.items.toMovieInfoListModel()

                    /**
                     * nextKey -> naver Api의 start 는 index 이고, 현재 display(limit) 가 10 이므로
                     * 10 이후에 start 값은 11 이 되어야함
                     * 스크롤될 때 마다 10개씩 가져온다.
                     */
                    return LoadResult.Page(
                        data = movieList,
                        prevKey = null,
                        nextKey = if (it.items.size >= limit) {
                            pageNumber + limit
                        } else {
                            null
                        }
                    )
                }
            }

            return LoadResult.Error(
                LoadException("영화 정보를 가져오는데 실패하였습니다.")
            )
        } catch (e: Exception) {
            Timber.e("paging catch Error -> ${e.message}")
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieInfoModel>): Int? {
        // 새로고침 될때 항상 전부 새로고침 되도록 null 을 return
        return null
    }
}