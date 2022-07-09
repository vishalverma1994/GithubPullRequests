package com.githubrequests.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.githubrequests.R
import com.githubrequests.api.model.GithubPullRequestsResponse
import com.githubrequests.databinding.AdapterGithubRequestsBinding
import com.githubrequests.extension.getTimeInUtcFormat

class GithubRequestsAdapter(private val onItemClick: (Int) -> Unit) :
    ListAdapter<GithubPullRequestsResponse, GithubRequestsAdapter.ViewHolder>(
        GITHUB_REQUEST_RESPONSE_DIFF_UTIL
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterGithubRequestsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(getItem(position))
    }

    inner class ViewHolder(private val binding: AdapterGithubRequestsBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(absoluteAdapterPosition)
            }
        }

        fun bindViews(response: GithubPullRequestsResponse) {
            Glide.with(context).load(response.user?.avatar_url.orEmpty())
                .apply(RequestOptions.circleCropTransform()).into(binding.ivUserProfile)

            binding.tvUserName.text = response.user?.login.orEmpty()
            binding.tvTitle.text = response.title.orEmpty()
            binding.tvCreatedAt.text =
                context.getString(R.string.created_at, response.created_at?.getTimeInUtcFormat().orEmpty())
            binding.tvClosedAt.text =
                context.getString(R.string.closed_at, response.closed_at?.getTimeInUtcFormat().orEmpty())
        }
    }

    companion object {
        private val GITHUB_REQUEST_RESPONSE_DIFF_UTIL = object : DiffUtil.ItemCallback<GithubPullRequestsResponse>() {
            override fun areItemsTheSame(
                oldItem: GithubPullRequestsResponse,
                newItem: GithubPullRequestsResponse
            ) = oldItem.base == newItem.base

            override fun areContentsTheSame(
                oldItem: GithubPullRequestsResponse,
                newItem: GithubPullRequestsResponse
            ) = oldItem == newItem

        }
    }
}
