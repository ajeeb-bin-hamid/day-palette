package com.day.palette.data.di

import com.day.palette.data.network.ApiService
import com.day.palette.data.prefs.SharedPrefsHelper
import com.day.palette.data.repository.RemoteRepositoryImpl
import com.day.palette.data.repository.UserPrefsRepositoryImpl
import com.day.palette.domain.repository.RemoteRepository
import com.day.palette.domain.repository.UserPrefsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder().baseUrl("https://date.nager.at/api/v3/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserPrefsRepository(sharedPrefsHelper: SharedPrefsHelper): UserPrefsRepository {
        return UserPrefsRepositoryImpl(sharedPrefsHelper)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(apiService: ApiService): RemoteRepository {
        return RemoteRepositoryImpl(apiService)
    }
}