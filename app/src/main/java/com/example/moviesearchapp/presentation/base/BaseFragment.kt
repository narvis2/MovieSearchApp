package com.example.moviesearchapp.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.moviesearchapp.presentation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
            showSnackBar(requireActivity().window?.decorView ?: binding.root, it.first, paddingVertical = it.second)
        }
    }

    protected fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    // Permission Dialog
    protected fun showPermissionDialog(
        context: Context,
        title: String? = null,
        message: String? = null,
        positiveBtnName: String? = null,
        negativeBtnName: String? = null,
        positiveAct: (() -> Unit)? = null,
        negativeAct: (() -> Unit)? = null,
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(positiveBtnName) { _, _ ->
            positiveAct?.let {
                it()
            }
        }
        builder.setNegativeButton(negativeBtnName) { dialogInterface, _ ->
            dialogInterface.dismiss()
            negativeAct?.let {
                it()
            }
        }
        builder.show()
    }
}