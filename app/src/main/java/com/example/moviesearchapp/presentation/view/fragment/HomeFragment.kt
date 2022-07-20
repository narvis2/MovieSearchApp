package com.example.moviesearchapp.presentation.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.moviesearchapp.R
import com.example.moviesearchapp.databinding.FragmentHomeBinding
import com.example.moviesearchapp.presentation.base.BaseFragment
import com.example.moviesearchapp.presentation.view.fragment.adapter.MovieInfoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    R.layout.fragment_home
) {
    override val viewModel: HomeViewModel by viewModels()

    private val adapter by lazy {
        MovieInfoAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = this.viewModel
    }
}