
class RemoteDataSourceImpl @Inject constructor(
    private val naverApiService: NaverApiServie
) : RemoteDataSource {
    override fun requestSearchMovie(query: String): Response<MovieResponse> {
        return naverApiService.searchMovie(query)
    }
}