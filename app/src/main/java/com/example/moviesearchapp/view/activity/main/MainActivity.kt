package com.example.moviesearchapp.view.activity.main

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.moviesearchapp.NavigationDirections
import com.example.moviesearchapp.R
import com.example.moviesearchapp.databinding.ActivityMainBinding
import com.example.moviesearchapp.base.BaseActivity
import com.example.moviesearchapp.utils.showSnackBar
import com.example.moviesearchapp.view.fragment.network.NetworkStatus
import com.example.moviesearchapp.view.fragment.network.NetworkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main
) {
    override val viewModel: MainViewModel by viewModels()

    private val networkViewModel: NetworkViewModel by viewModels()

    private lateinit var navController: NavController

    private var backKeyPressedTime: Long = 0

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = this.viewModel

        navController = navHostFragment.navController

        onBackPressedDispatcher.addCallback(this) {
            if (navController.previousBackStackEntry == null || !navController.popBackStack()) {
                if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                    backKeyPressedTime = System.currentTimeMillis()
                    showSnackBar(binding.root, getString(R.string.main_back_pressed), 2000)
                    return@addCallback
                } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                    finish()
                    return@addCallback
                }
            }
        }

        // Network Callback 등록
        networkViewModel.register(true)
        initNetworkViewModelCallback()
    }

    private fun initNetworkViewModelCallback() = with (networkViewModel) {
        currentNetworkStatus.observe(this@MainActivity) {
            when (it) {
                NetworkStatus.CONNECT_NETWORK -> {
                    // TODO: 네트워크 연결 시 동작 등록..
                }
                // 네트워크가 끊기거나 Error 일 경우 NetworkDialogFragment 보여줌
                else -> {
                    navController.navigate(
                        NavigationDirections.actionGlobalNetworkDialogFragment(it)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        // Network Callback 제거
        networkViewModel.unRegister()
        super.onDestroy()
    }
}