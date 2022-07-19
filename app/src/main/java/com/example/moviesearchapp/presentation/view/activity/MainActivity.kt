package com.example.moviesearchapp.presentation.view.activity

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.moviesearchapp.R
import com.example.moviesearchapp.databinding.ActivityMainBinding
import com.example.moviesearchapp.presentation.base.BaseActivity
import com.example.moviesearchapp.presentation.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main
) {
    override val viewModel: MainViewModel by viewModels()

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
    }
}