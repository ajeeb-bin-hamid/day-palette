package com.day.palette.home.domain.di

import com.day.palette.home.data.network.HomeApiService
import com.day.palette.home.data.prefs.HomePrefsHelper
import com.day.palette.home.data.repository.HomeHomeRemoteRepositoryImpl
import com.day.palette.home.data.repository.HomePrefsRepositoryImpl
import com.day.palette.home.domain.repository.HomeRemoteRepository
import com.day.palette.home.domain.repository.HomePrefsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HomeDomainModule {

    @Provides
    @Singleton
    fun provideHomePrefsRepository(homePrefsHelper: HomePrefsHelper): HomePrefsRepository =
        HomePrefsRepositoryImpl(homePrefsHelper)

    @Provides
    @Singleton
    fun provideHomeRemoteRepository(homeApiService: HomeApiService): HomeRemoteRepository =
        HomeHomeRemoteRepositoryImpl(homeApiService)
}