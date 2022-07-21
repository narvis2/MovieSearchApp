package com.example.moviesearchapp.view.activity.web

import androidx.lifecycle.viewModelScope
import com.example.moviesearchapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor() : BaseViewModel() {

    val showTitle = MutableStateFlow(false)

    val showProgress = MutableStateFlow(false)

    val title = MutableStateFlow("")

    private val _close = Channel<Unit>(Channel.CONFLATED)
    val close = _close.receiveAsFlow()

    fun onBackBtnClick() = viewModelScope.launch {
        _close.send(Unit)
    }
}