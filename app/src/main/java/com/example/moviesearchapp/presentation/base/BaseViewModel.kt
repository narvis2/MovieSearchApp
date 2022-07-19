package com.example.moviesearchapp.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _hideKeyboard = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val hideKeyboard: SharedFlow<Boolean>
        get() = _hideKeyboard

    private val _backStack = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val backStack: SharedFlow<Boolean>
        get() = _backStack

    private val _showSnackBar = Channel<Pair<String, Int>>(Channel.CONFLATED)
    val showSnackBar = _showSnackBar.receiveAsFlow()

    fun showLoadingDialog() = viewModelScope.launch {
        _isLoading.emit(true)
    }

    fun hideLoadingDialog() = viewModelScope.launch{
        _isLoading.emit(false)
    }

    fun hideKeyboard() = viewModelScope.launch {
        _hideKeyboard.emit(true)
    }

    fun showKeyboard() = viewModelScope.launch {
        _hideKeyboard.emit(false)
    }

    fun popBackStack() = viewModelScope.launch {
        _backStack.emit(true)
    }

    fun navigateUp() = viewModelScope.launch {
        _backStack.emit(false)
    }

    fun showSnackBar(message: String, paddingVertical: Int = 10) = viewModelScope.launch {
        _showSnackBar.send(message to paddingVertical)
    }
}