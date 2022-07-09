package com.githubrequests.api

import com.githubrequests.BuildConfig
import com.githubrequests.api.model.GithubPullRequestsAbstractResponse
import com.githubrequests.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * class connects with the network and give result either success or failure
 */
@Singleton
class NetworkDataSource @Inject constructor(private val apiClient: ApiClient) {

    suspend fun fetchGithubRequests(owner: String, repo: String, state: String): Flow<Either<Failure, Success>> = flow {
        try {
            apiClient.fetchGithubPullRequests(BuildConfig.GITHUB_TOKEN, HEADER_ACCEPT_VALUE, owner, repo, state).let { response ->
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { githubRequest ->
                        emit(Either.Right(GithubPullRequestsAbstractResponse(githubRequest)))
                    }
                } else {
                    emit(Either.Left(Failure.ServerError(ERROR_MESSAGE)))
                }
            }
        } catch (e: HttpException) {
            emit(Either.Left(Failure.ServerError(e.message())))
        } catch (e: IOException) {
            emit(Either.Left(Failure.NetworkConnectionError))
        } catch (e: Exception) {
            emit(Either.Left(Failure.GenericError(e)))
        }
    }
}