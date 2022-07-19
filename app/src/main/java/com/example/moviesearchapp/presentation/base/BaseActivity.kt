package com.example.moviesearchapp.presentation.base

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.moviesearchapp.BuildConfig
import com.example.moviesearchapp.presentation.utils.observeOnLifecycleDestroy

abstract class BaseActivity<B: ViewDataBinding, VM: BaseViewModel>(
    @LayoutRes private val layoutResId: Int
): AppCompatActivity() {

    protected val binding: B by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView(this, layoutResId)
    }

    init {
        addOnContextAvailableListener { binding.notifyChange() }
    }

    abstract val viewModel: VM

    private var loadingDialog: AlertDialog? = null
    private var loadingCnt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        // 화면 캡쳐 방지
        if (!BuildConfig.DEBUG) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        baseViewModelCallback()
    }

    private fun baseViewModelCallback() {
        viewModel.isLoading.observeOnLifecycleDestroy(this) { show ->
            if (show) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        }
    }

    fun showLoadingDialog() {
        loadingCnt++

//        if(loadingDialog?.isShowing == true) {
//            return
//        }
//
//        val dialogBuilder =
//            AlertDialog.Builder(this, R.style.AlertDialogTheme)
//        dialogBuilder.setCancelable(false)
//        val dialogView = this.layoutInflater.inflate(
//            R.layout.progress_dialog_view,
//            null
//        )
//        dialogBuilder.setView(dialogView)
//        loadingDialog = dialogBuilder.create()
//        loadingDialog?.show()
    }

    fun hideLoadingDialog() {
        if (loadingCnt >= 1) {
            loadingCnt--

            if (loadingCnt == 0 && loadingDialog != null) {
                loadingDialog?.dismiss()
                loadingDialog = null
            }
        }
    }

    override fun onDestroy() {
        hideLoadingDialog()
        super.onDestroy()
    }
}