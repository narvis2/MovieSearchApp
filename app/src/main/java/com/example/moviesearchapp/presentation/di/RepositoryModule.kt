
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNaverRepository(
        remoteDataSource: RemoteDataSource
    ): NaverRepository {
        return NaverRepositoryImpl(remoteDataSource)
    }
}