package com.example.moviesearchapp.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.moviesearchapp.presentation.utils.hideKeyboard
import com.example.moviesearchapp.presentation.utils.observeOnLifecycleStop
import com.example.moviesearchapp.presentation.utils.showKeyboard
import com.example.moviesearchapp.presentation.utils.showSnackBar

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes private val layoutResId: Int
) : Fragment() {

    private lateinit var _binding: B
    protected val binding: B
        get() = _binding

    protected lateinit var thisContext: Context

    abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseViewModelCallback()
    }

    private fun baseViewModelCallback() {
        viewModel.hideKeyboard.observeOnLifecycleStop(viewLifecycleOwner) { isHide ->
            if (isHide) {
                requireContext().let {
                    context?.hideKeyboard(binding.root)
                }
            } else {
                requireContext().let {
                    context?.showKeyboard(binding.root)
                }
            }
        }

        viewModel.backStack.observeOnLifecycleStop(viewLifecycleOwner) {
            if (it) {
                findNavController().popBackStack()
            } else {
                findNavController().navigateUp()
            }
        }

        viewModel.showSnackBar.observeOnLifecycleStop(viewLifecycleOwner) {
            showSnackBar(
                requireActivity().window?.decorView ?: binding.root,
                it.first,
                paddingVertical = it.second
            )
        }
    }
}