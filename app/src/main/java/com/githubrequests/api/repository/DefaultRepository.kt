package com.githubrequests.api.repository

import com.githubrequests.api.NetworkDataSource
import com.githubrequests.api.model.GithubPullRequestsAbstractResponse
import com.githubrequests.domain.repository.GithubRequestRepository
import com.githubrequests.utils.Either
import com.githubrequests.utils.Failure
import com.githubrequests.utils.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class DefaultRepository(private val networkDataSource: NetworkDataSource) : GithubRequestRepository {

    override suspend fun fetchGithubRequests(
        owner: String,
        repo: String,
        state: String
    ): Flow<Either<Failure, Success>> = flow {
        emit(Either.Right(Success.Loading))
        try {
            fetchGithubPullRequestsFromNetwork(onError = { error ->
                emit(error)
            }, onSuccess =  { state ->
                when(state.data) {
                    is GithubPullRequestsAbstractResponse -> emit(Either.Right(state.data))
                    else -> {
                        emit(state)
                    }
                }
            }, owner = owner, repo = repo, state = state)
        } catch (e: HttpException) {
            emit(Either.Left(Failure.ServerError(e.message())))
        } catch (e: IOException) {
            emit(Either.Left(Failure.NetworkConnectionError))
        } catch (e: Exception) {
            emit(Either.Left(Failure.GenericError(e)))
        }
    }

    private suspend fun fetchGithubPullRequestsFromNetwork(
        owner: String, repo: String, state: String,
        onError: suspend (Either.Left<Failure>) -> Unit,
        onSuccess: suspend (Either.Right<Success>) -> Unit
    ) {
        networkDataSource.fetchGithubRequests(owner, repo, state).collect {
            when (it) {
                is Either.Left -> onError(it)
                is Either.Right -> onSuccess(it)
            }
        }
    }
}