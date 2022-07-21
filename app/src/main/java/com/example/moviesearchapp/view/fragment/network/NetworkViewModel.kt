package com.example.moviesearchapp.view.fragment.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    private val context = application

    private var manager: ConnectivityManager? = null

    private var isNetworkConn = false

    private var isWifiConn = false

    private var isCellularConn = false

    private val _currentNetworkStatus = MutableLiveData<NetworkStatus>()
    val currentNetworkStatus: LiveData<NetworkStatus>
        get() = _currentNetworkStatus

    val networkStatus = MutableStateFlow(NetworkStatus.CONNECT_ERROR)

    private val _networkAction = Channel<Boolean>(Channel.CONFLATED)
    val networkAction = _networkAction.receiveAsFlow()

    private val wifiNetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isWifiConn = true
            changeNetworkStatus()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isWifiConn = false
            changeNetworkStatus()
        }
    }

    private val cellularNetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isCellularConn = true
            changeNetworkStatus()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isCellularConn = false
            changeNetworkStatus()
        }
    }

    private val defaultNetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isNetworkConn = true
            changeNetworkStatus()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isNetworkConn = false
            changeNetworkStatus()
        }
    }

    fun changeNetworkStatus() = viewModelScope.launch {
        networkStatus.value = when {
            isNetworkConn && (isWifiConn || isCellularConn) -> {
                NetworkStatus.CONNECT_NETWORK
            }
            else -> {
                NetworkStatus.DISCONNECT_NETWORK
            }
        }

        if (networkStatus.value != currentNetworkStatus.value) {
            _currentNetworkStatus.value = networkStatus.value
        }
    }

    fun register(checkNetworkStatus: Boolean) {
        manager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerDefaultNetwork()
        } else {
            isNetworkConn = true
        }
        registerWifi()
        registerCellular()

        if (checkNetworkStatus) {
            checkActiveNetwork()
            changeNetworkStatus()
        }
    }

    fun unRegister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            unRegisterDefaultNetwork()
        }

        unRegisterWifi()
        unRegisterCellular()
        manager = null
    }

    private fun checkActiveNetwork() {
        manager?.activeNetwork?.let {
            isNetworkConn = true
            isWifiConn = hasTransport(it, NetworkCapabilities.TRANSPORT_WIFI)
            isCellularConn = hasTransport(it, NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }

    private fun hasTransport(network: Network, transport: Int): Boolean =
        manager?.getNetworkCapabilities(network)?.hasTransport(transport) ?: false

    private fun registerWifi() {
        manager?.registerNetworkCallback(
            createNetworkRequest(NetworkCapabilities.TRANSPORT_WIFI),
            wifiNetworkCallback
        )
    }

    private fun unRegisterWifi() {
        manager?.unregisterNetworkCallback(wifiNetworkCallback)
    }

    private fun registerCellular() {
        manager?.registerNetworkCallback(
            createNetworkRequest(NetworkCapabilities.TRANSPORT_CELLULAR),
            cellularNetworkCallback
        )
    }

    private fun unRegisterCellular() {
        manager?.unregisterNetworkCallback(cellularNetworkCallback)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun registerDefaultNetwork() {
        manager?.registerDefaultNetworkCallback(defaultNetworkCallback)
    }

    private fun unRegisterDefaultNetwork() {
        manager?.unregisterNetworkCallback(defaultNetworkCallback)
    }

    private fun createNetworkRequest(capability: Int): NetworkRequest {
        return NetworkRequest.Builder()
            .addTransportType(capability)
            .build()
    }

    fun onReTryClick() = viewModelScope.launch {
        when (networkStatus.value) {
            NetworkStatus.DISCONNECT_NETWORK,
            NetworkStatus.CONNECT_ERROR -> {
                _networkAction.send(false)
            }

            NetworkStatus.CONNECT_NETWORK -> {
                _networkAction.send(true)
            }
        }
    }
}