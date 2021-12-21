package com.turtle.yososuwhere.presentation.view.home

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.turtle.yososuwhere.databinding.ListItemLoadingItemBinding

class LoadingStateViewHolder(
    private val binding: ListItemLoadingItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.progressBar.isVisible = loadState is LoadState.Loading
    }
}