package com.githubrequests.domain.usecases

import app.cash.turbine.test
import com.githubrequests.api.model.GithubPullRequestsAbstractResponse
import com.githubrequests.api.repository.FakeRepository
import com.githubrequests.utils.*
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FetchGithubRequestsUseCaseTest {

    private lateinit var fetchGithubRequestsUseCase: FetchGithubRequestsUseCase
    private lateinit var repo: FakeRepository

    @Before
    fun setup() {
        repo = FakeRepository()
        fetchGithubRequestsUseCase = FetchGithubRequestsUseCase(repo)
    }

    @Test
    fun `in successful fetch return CurrencyDto object -`() = runBlockingTest {
        val job = launch {
            fetchGithubRequestsUseCase.invoke(OWNER, REPO, CLOSED).test {
                val result: Either<Failure, Success> = awaitItem()
                when (result) {
                    is Either.Right -> {
                        Truth.assertThat(result.data).isInstanceOf(GithubPullRequestsAbstractResponse::class.java)
                    }
                    else -> Unit
                }
                cancelAndConsumeRemainingEvents()
            }
        }

        job.cancelAndJoin()
    }

    @Test
    fun `in some error fetch return Failure Type object`() = runBlockingTest {
        repo.setErrorOn(true)
        val job = launch {
            fetchGithubRequestsUseCase.invoke(OWNER, REPO, CLOSED).test {
                val result: Either<Failure, Success> = awaitItem()
                when (result) {
                    is Either.Left -> {
                        Truth.assertThat(result.data).isInstanceOf(Failure.ServerError::class.java)
                    }
                    else -> Unit
                }
                cancelAndConsumeRemainingEvents()
            }
        }

        job.cancelAndJoin()
    }
}