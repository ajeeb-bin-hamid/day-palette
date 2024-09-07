package com.day.palette.common.presentation.di

import android.content.Context
import android.util.DisplayMetrics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CommonPresentationModule {

    @Provides
    fun provideScreenDimensions(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }
}