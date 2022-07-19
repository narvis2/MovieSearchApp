
@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetMovieListPagingDataUseCase(
        naverRepository: NaverRepository
    ): GetMovieListPagingDataUseCase {
        return GetMovieListPagingDataUseCase(naverRepository)
    }
}