package com.githubrequests.di

import com.githubrequests.domain.dispatcher.CoroutineDispatcherProvider
import com.githubrequests.domain.dispatcher.RealCoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * this module provide dispatcher for Coroutines for production
 */
@InstallIn(SingletonComponent::class)
@Module
class DispatcherModule {
    @Provides
    @Singleton
    fun providesCoroutineDispatcher(): CoroutineDispatcherProvider {
        return RealCoroutineDispatcherProvider()
    }
}