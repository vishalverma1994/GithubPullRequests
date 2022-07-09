package com.githubrequests.domain.repository

import com.githubrequests.utils.Either
import com.githubrequests.utils.Failure
import com.githubrequests.utils.Success
import kotlinx.coroutines.flow.Flow

interface GithubRequestRepository {

    suspend fun fetchGithubRequests(owner: String, repo: String, state: String): Flow<Either<Failure, Success>>
}