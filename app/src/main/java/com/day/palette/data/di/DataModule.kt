package com.day.palette.data.di

import android.content.Context
import com.day.palette.data.prefs.SharedPrefsHelper
import com.day.palette.data.repository.UserPrefsRepositoryImpl
import com.day.palette.domain.repository.UserPrefsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideSharedPrefsHelper(context: Context): SharedPrefsHelper {
        return SharedPrefsHelper(context)
    }

    @Provides
    @Singleton
    fun provideUserPrefsRepository(sharedPrefsHelper: SharedPrefsHelper): UserPrefsRepository {
        return UserPrefsRepositoryImpl(sharedPrefsHelper)
    }
}