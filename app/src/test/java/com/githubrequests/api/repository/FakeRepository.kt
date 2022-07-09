package com.githubrequests.api.repository

import com.githubrequests.api.model.GithubPullRequestsAbstractResponse
import com.githubrequests.api.model.GithubPullRequestsResponse
import com.githubrequests.domain.repository.GithubRequestRepository
import com.githubrequests.utils.Either
import com.githubrequests.utils.Failure
import com.githubrequests.utils.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : GithubRequestRepository {

    private var simulateError = false

    fun setErrorOn(_errorOn: Boolean) {
        simulateError = _errorOn
    }

    override suspend fun fetchGithubRequests(
        owner: String,
        repo: String,
        state: String
    ): Flow<Either<Failure, Success>> = flow {
        if (simulateError) {
            emit(Either.Left(Failure.ServerError("some server failure")))
        } else {
            emit(Either.Right(GithubPullRequestsAbstractResponse(emptyList<GithubPullRequestsResponse>())))

        }
    }
}