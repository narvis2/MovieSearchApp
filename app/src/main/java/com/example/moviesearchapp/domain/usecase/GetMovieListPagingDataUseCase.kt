
class GetMovieListPagingDataUseCase @Inject constructor(
    private val naverRepository: NaverRepository
) {
    operator fun invoke(
        query: StateFlow<String>
    ): Flow<PagingData<MoiveInfoModel>> {
        return naverRepository.getMovieList(query)
    }
}