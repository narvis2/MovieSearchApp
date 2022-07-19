interface RemoteDataSource {
    suspend fun requestSearchMovie(query: String): Response<MovieResponse>
}