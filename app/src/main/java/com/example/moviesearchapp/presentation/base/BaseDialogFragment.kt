package com.example.moviesearchapp.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.moviesearchapp.presentation.utils.hideKeyboard
import com.example.moviesearchapp.presentation.utils.observeOnLifecycleStop
import com.example.moviesearchapp.presentation.utils.showKeyboard
import com.example.moviesearchapp.presentation.utils.showSnackBar

abstract class BaseDialogFragment<B: ViewDataBinding, VM: ViewModel>(
    @LayoutRes private val layoutResId: Int,
    @StyleRes private val styleResId: Int?
) : DialogFragment() {

    private lateinit var _binding: B
    protected val binding: B
        get() = _binding

    abstract val viewModel: VM

    protected lateinit var thisContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        styleResId?.let {
            setStyle(STYLE_NORMAL, it)
        }
    }

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
        if (viewModel is BaseViewModel) {
            (viewModel as BaseViewModel).hideKeyboard.observeOnLifecycleStop(viewLifecycleOwner) { isHide ->
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
        }

        if (viewModel is BaseViewModel) {
            (viewModel as BaseViewModel).backStack.observeOnLifecycleStop(viewLifecycleOwner) {
                if (it) {
                    findNavController().popBackStack()
                } else {
                    findNavController().navigateUp()
                }
            }
        }

        if (viewModel is BaseViewModel) {
            (viewModel as BaseViewModel).showSnackBar.observeOnLifecycleStop(viewLifecycleOwner) {
                showSnackBar(parentFragment?.view ?: binding.root, it.first, paddingVertical = it.second)
            }
        }
    }
}