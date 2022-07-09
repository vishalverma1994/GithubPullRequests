package com.githubrequests.api.model

import com.githubrequests.utils.Success

data class GithubPullRequestsAbstractResponse(
    val githubPullRequestList: List<GithubPullRequestsResponse>
) : Success.ViewState()
