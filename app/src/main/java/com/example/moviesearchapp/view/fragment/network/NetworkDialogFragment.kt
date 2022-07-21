package com.example.moviesearchapp.view.fragment.network

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.moviesearchapp.R
import com.example.moviesearchapp.databinding.FragmentNetworkDialogBinding
import com.example.moviesearchapp.base.BaseDialogFragment
import com.example.moviesearchapp.utils.observeInLifecycleStop
import com.example.moviesearchapp.utils.showSnackBar
import com.example.moviesearchapp.view.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NetworkDialogFragment : BaseDialogFragment<FragmentNetworkDialogBinding, NetworkViewModel>(
    R.layout.fragment_network_dialog,
    R.style.BackgroundTransparentBaseDialogStyle
) {

    override val viewModel: NetworkViewModel by activityViewModels()

    private val args: NetworkDialogFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.networkStatus.value = args.networkState

        networkViewModelCallback()
    }

    private fun networkViewModelCallback() = with (viewModel) {
        currentNetworkStatus.observe(viewLifecycleOwner) {
            when(it) {
                NetworkStatus.CONNECT_NETWORK -> {
                    findNavController().popBackStack()
                }
                else -> {}
            }
        }

        networkAction.onEach {
            if (it) {
                findNavController().popBackStack()
            } else {
                showSnackBar(binding.root, getString(R.string.str_disabled_network))
            }
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    override fun onStart() {
        super.onStart()
        val d = dialog
        if (activity is MainActivity) {
            d?.setCancelable(false)
        }
    }
}