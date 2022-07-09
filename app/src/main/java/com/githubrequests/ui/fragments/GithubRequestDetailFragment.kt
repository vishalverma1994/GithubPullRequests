package com.githubrequests.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.githubrequests.R
import com.githubrequests.api.model.GithubPullRequestsResponse
import com.githubrequests.databinding.FragmentGithubRequestDetailBinding
import com.githubrequests.extension.getTimeInUtcFormat
import com.githubrequests.utils.ARG_1
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GithubRequestDetailFragment : Fragment() {

    private lateinit var binding: FragmentGithubRequestDetailBinding
    private lateinit var githubPullRequestsResponse: GithubPullRequestsResponse

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGithubRequestDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractData()
        setUI()
    }

    private fun extractData() {
        requireArguments().let {
            githubPullRequestsResponse = it.get(ARG_1) as GithubPullRequestsResponse
        }
    }

    private fun setUI() {
        if (::githubPullRequestsResponse.isInitialized) {
            Glide.with(requireContext()).load(githubPullRequestsResponse.user?.avatar_url.orEmpty())
                .apply(RequestOptions.circleCropTransform()).into(binding.ivUserProfile)

            binding.tvUserName.text = githubPullRequestsResponse.user?.login.orEmpty()
            binding.tvTitle.text = githubPullRequestsResponse.title.orEmpty()
            binding.tvClosedAt.text =
                getString(R.string.closed_at, githubPullRequestsResponse.closed_at?.getTimeInUtcFormat().orEmpty())
        }
    }
}