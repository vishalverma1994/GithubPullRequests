package com.githubrequests.di

import com.githubrequests.api.NetworkDataSource
import com.githubrequests.api.repository.DefaultRepository
import com.githubrequests.domain.repository.GithubRequestRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    @Singleton
    fun provideCurrencyRepository(
        networkDataSource: NetworkDataSource
    ): GithubRequestRepository {
        return DefaultRepository(networkDataSource)
    }
}