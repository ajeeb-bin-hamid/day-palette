package com.day.palette.presentation.di

import android.content.Context
import android.util.DisplayMetrics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    fun provideScreenDimensions(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }
}