package com.example.moviesearchapp.presentation

import android.app.Application
import com.example.moviesearchapp.BuildConfig
import com.example.moviesearchapp.presentation.utils.TimberDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MovieApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugTree())
        }
    }
}