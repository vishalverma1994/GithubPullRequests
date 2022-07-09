package com.githubrequests.api

import com.githubrequests.api.model.GithubPullRequestsResponse
import com.githubrequests.utils.FETCH_GITHUB_PULL_REQUESTS
import com.githubrequests.utils.HEADER_ACCEPT
import com.githubrequests.utils.HEADER_AUTH_TOKEN
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET(FETCH_GITHUB_PULL_REQUESTS)
    suspend fun fetchGithubPullRequests(
        @Header(HEADER_AUTH_TOKEN) token: String,
        @Header(HEADER_ACCEPT) header_accept: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("state") state: String
    ): Response<List<GithubPullRequestsResponse>>

}