package com.day.palette.domain.di

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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideUserPrefsRepository(sharedPrefsHelper: SharedPrefsHelper): UserPrefsRepository =
        UserPrefsRepositoryImpl(sharedPrefsHelper)

    @Provides
    @Singleton
    fun provideRemoteRepository(apiService: ApiService): RemoteRepository =
        RemoteRepositoryImpl(apiService)
}