package com.presta.customer

import kotlinx.coroutines.flow.MutableStateFlow

expect class ConnectivityStatus {
    val isNetworkConnected: MutableStateFlow<Boolean>
    fun start()
    fun stop()
    fun getStatus(success: (Boolean) -> Unit)
}