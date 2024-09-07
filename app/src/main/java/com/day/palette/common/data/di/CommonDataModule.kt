package com.day.palette.common.data.di

import com.day.palette.common.data.network.NetworkInterceptor
import com.day.palette.common.data.utils.AppIdlingResource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonDataModule {

    @Provides
    @Singleton
    fun provideAppIdlingResource(): AppIdlingResource = AppIdlingResource()

    @Provides
    @Singleton
    fun provideNetworkInterceptor(appIdlingResource: AppIdlingResource): NetworkInterceptor =
        NetworkInterceptor(appIdlingResource)

    @Provides
    @Singleton
    fun provideOkHttpClient(networkInterceptor: NetworkInterceptor): OkHttpClient =
        OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(networkInterceptor).build()
}