package com.githubrequests.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.githubrequests.R
import androidx.navigation.fragment.findNavController
import com.githubrequests.api.model.GithubPullRequestsAbstractResponse
import com.githubrequests.api.model.GithubPullRequestsResponse
import com.githubrequests.databinding.FragmentGithubRequestsBinding
import com.githubrequests.ui.adapter.GithubRequestsAdapter
import com.githubrequests.utils.ARG_1
import com.githubrequests.utils.ERROR_MESSAGE
import com.githubrequests.utils.Failure
import com.githubrequests.utils.Success
import com.githubrequests.viewmodels.GithubRequestsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GithubRequestsFragment: Fragment() {

    private lateinit var binding: FragmentGithubRequestsBinding
    private val githubRequestsViewModel: GithubRequestsViewModel by viewModels()
    private val githubRequestsAdapter by lazy { GithubRequestsAdapter(::onItemClick) }
    private var githubPullRequestList = emptyList<GithubPullRequestsResponse>()

    companion object {
        private val TAG = GithubRequestsFragment::class.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGithubRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        setGithubRequestsAdapter()
        githubRequestsViewModel.fetchGithubRequests()
    }

    private fun initComponents() {
        githubRequestsViewModel.successEventFlow.observeFlow {
            handleSuccess(it)
        }

        githubRequestsViewModel.errorEventFlow.observeFlow {
            handleFailure(it)
        }
    }

    private fun setGithubRequestsAdapter() {
        binding.rvGithubRequests.apply {
            setHasFixedSize(true)
            adapter = githubRequestsAdapter
        }
    }

    private fun onItemClick(position: Int) {
        if (githubPullRequestList.isNotEmpty()) {
            val bundle = bundleOf(ARG_1 to githubPullRequestList[position])
            findNavController().navigate(R.id.action_myGithubRequestsFragment_to_myGithubRequestDetailFragment, bundle)
        }
    }

    /**
     * handles the success state cases
     */
    private fun handleSuccess(success: Success) {
        binding.progressBar.isVisible = success is Success.Loading
        updateUi()
        when (success) {
            Success.Idle -> Log.d(TAG, "getGithubRequests: idle ")
            is GithubPullRequestsAbstractResponse -> {
                Log.d(TAG, "getGithubRequests: ${success.githubPullRequestList} ")
                this.githubPullRequestList = success.githubPullRequestList
                githubRequestsAdapter.submitList(githubPullRequestList)
            }
            else -> Unit
        }
    }

    /**
     * handle all the failure possibilities
     */
    private fun handleFailure(failure: Failure) {
        binding.progressBar.isVisible = false

        when (failure) {
            is Failure.GenericError -> {
                Log.d(TAG, "getGithubRequests: ${failure.exception.localizedMessage}")
                updateUi(true, failure.exception.localizedMessage ?: ERROR_MESSAGE)
            }
            Failure.NetworkConnectionError -> {
                Log.d(TAG, "getGithubRequests: network error")
                updateUi(true, getString(R.string.network_connection_error))

            }
            is Failure.ServerError -> {
                Log.d(TAG, "getGithubRequests: ${failure.error} ")
                updateUi(true, failure.error)
            }
            else -> Unit
        }
    }

    private fun updateUi(isError: Boolean = false, errorMsg: String = "") {
        if (isError) {
            binding.errorLayout.root.isVisible = true
            binding.errorLayout.tvError.text = errorMsg
            binding.rvGithubRequests.isVisible = false
        } else {
            binding.errorLayout.root.isVisible = false
            binding.errorLayout.tvError.text = ""
            binding.rvGithubRequests.isVisible = true
        }

    }

    /**
     * helper wrapper method for collecting data from flow
     */
    private fun <T> Flow<T>.observeFlow(action: suspend (T) -> Unit) {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@observeFlow.collect {
                    action(it)
                }
            }
        }
    }
}