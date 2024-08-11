package com.day.palette.data.di

import com.day.palette.BuildConfig
import com.day.palette.data.network.ApiService
import com.day.palette.data.network.IdlingResourceInterceptor
import com.day.palette.data.utils.AppIdlingResource
import com.day.palette.data.prefs.SharedPrefsHelper
import com.day.palette.data.repository.RemoteRepositoryImpl
import com.day.palette.data.repository.UserPrefsRepositoryImpl
import com.day.palette.domain.repository.RemoteRepository
import com.day.palette.domain.repository.UserPrefsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideNetworkIdlingResource(): AppIdlingResource = AppIdlingResource()

    @Provides
    @Singleton
    fun provideIdlingResourceInterceptor(appIdlingResource: AppIdlingResource): IdlingResourceInterceptor =
        IdlingResourceInterceptor(appIdlingResource)

    @Provides
    @Singleton
    fun provideOkHttpClient(idlingResourceInterceptor: IdlingResourceInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(idlingResourceInterceptor).build()

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideUserPrefsRepository(sharedPrefsHelper: SharedPrefsHelper): UserPrefsRepository =
        UserPrefsRepositoryImpl(sharedPrefsHelper)

    @Provides
    @Singleton
    fun provideRemoteRepository(apiService: ApiService): RemoteRepository =
        RemoteRepositoryImpl(apiService)
}