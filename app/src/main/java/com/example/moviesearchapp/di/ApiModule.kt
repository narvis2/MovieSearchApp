package com.example.moviesearchapp.di

import com.example.data.api.NaverApiService
import com.example.data.repository.datasource.RemoteDataSource
import com.example.data.repository.datasourceImpl.RemoteDataSourceImpl
import com.example.moviesearchapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        naverApiService: NaverApiService
    ): RemoteDataSource {
        return RemoteDataSourceImpl(
            naverApiService
        )
    }

    @Singleton
    @Provides
    fun provideNaverApiService(
        retrofit: Retrofit
    ): NaverApiService {
        return retrofit.create(NaverApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        converter: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_HOST)
            .client(client)
            .addConverterFactory(converter)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        interceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(logging)
        }
        addInterceptor(interceptor)
    }.build()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    /**
     * @param X-Naver-Client-Id : 네이버에서 받은 Client Id 를 입력합니다.
     * @param X-Naver-Client-Secret : 네이버에서 받은 Client Secret 을 입력합니다.
     */
    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor = Interceptor { chain ->
        val response = chain.proceed(
            chain.request().newBuilder().apply {
                addHeader("X-Naver-Client-Id", BuildConfig.NAVE_CLIENT_ID)
                addHeader("X-Naver-Client-Secret", BuildConfig.NAVE_CLIENT_SECRET)
            }.build()
        )

        response
    }
}