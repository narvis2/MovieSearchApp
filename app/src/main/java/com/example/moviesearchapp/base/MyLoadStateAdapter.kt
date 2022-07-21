package com.example.moviesearchapp.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapp.R
import com.example.moviesearchapp.databinding.ItemLoadStateFooterViewBinding

class MyLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<MyLoadStateAdapter.LoadStateViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding: ItemLoadStateFooterViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_load_state_footer_view,
            parent,
            false
        )
        val errorString = parent.context.resources.getString(R.string.str_load_state_error)
        return LoadStateViewHolder(binding, errorString)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(
        private val binding: ItemLoadStateFooterViewBinding,
        private val errorString: String
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            // endOfPaginationReached -> 로드할 데이터가 더 있으면 false 이고, 로드할 데이터가 없으면 true
            if (loadState is LoadState.NotLoading && loadState.endOfPaginationReached) {
                binding.progressBar.isVisible = false
                binding.retryButton.isVisible = false
                binding.errorMsg.isVisible = false
                return
            }

            if (loadState is LoadState.Error) {
                binding.errorMsg.text = errorString
            }

            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }
}