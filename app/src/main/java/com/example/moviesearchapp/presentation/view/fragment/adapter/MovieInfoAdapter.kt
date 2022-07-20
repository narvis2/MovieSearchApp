package com.example.moviesearchapp.presentation.view.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapp.R
import com.example.moviesearchapp.databinding.MovieInfoItemBinding
import com.example.moviesearchapp.domain.model.MovieInfoModel

class MovieInfoAdapter : PagingDataAdapter<MovieInfoModel, MovieInfoAdapter.MovieInfoViewHolder>(diffUtil) {

    interface OnItemClickListener {
        fun onClick(item: MovieInfoModel, position: Int)
    }

    interface MovieRankingPresenter {
        fun onRootClick(view: View?)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieInfoViewHolder {
        val binding: MovieInfoItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.movie_info_item,
            parent,
            false
        )

        return MovieInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieInfoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, listener)
        }
    }

    inner class MovieInfoViewHolder(
        private val binding: MovieInfoItemBinding
    ) : RecyclerView.ViewHolder(binding.root), MovieRankingPresenter {

        private var listener: OnItemClickListener? = null

        fun bind(info: MovieInfoModel, listener: OnItemClickListener?) {
            binding.info = info
            binding.presenter = this
            this.listener = listener
            binding.executePendingBindings()
        }

        override fun onRootClick(view: View?) {
            binding.info?.let {
                listener?.onClick(it, bindingAdapterPosition)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<MovieInfoModel>() {
            override fun areItemsTheSame(
                oldItem: MovieInfoModel,
                newItem: MovieInfoModel
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: MovieInfoModel,
                newItem: MovieInfoModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}