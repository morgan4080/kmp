package com.presta.customer

import com.github.ln_12.library.ConnectivityStatus
import kotlinx.coroutines.flow.MutableStateFlow

actual class SharedStatus {
    private val connectivityStatus: ConnectivityStatus = ConnectivityStatus()
    actual val current: MutableStateFlow<Boolean> = connectivityStatus.isNetworkConnected

    actual fun start() {
        connectivityStatus.start()
    }

    actual fun stop() {
        connectivityStatus.stop()
    }

    fun getStatus(success: (Boolean) -> Unit) = connectivityStatus.getStatus(success)
}