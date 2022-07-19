package com.example.moviesearchapp.data.model

class NaverRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : NaverRepository {
    override fun getMovieList(query: StateFlow<String>): Flow<PagingData<MovieInfoModel>> {
        // TODO: 7/19/22 Pager 설정
    }
}