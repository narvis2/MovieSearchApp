package com.example.moviesearchapp.base

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.moviesearchapp.BuildConfig

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
    }
}