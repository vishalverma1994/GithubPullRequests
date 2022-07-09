package com.githubrequests.domain.usecases

import com.githubrequests.domain.repository.GithubRequestRepository
import com.githubrequests.utils.Either
import com.githubrequests.utils.Failure
import com.githubrequests.utils.Success
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class FetchGithubRequestsUseCase @Inject constructor(private val repository: GithubRequestRepository) {

    suspend fun invoke(owner: String, repo: String, state: String): Flow<Either<Failure, Success>> {
        return repository.fetchGithubRequests(owner, repo, state)
    }
}