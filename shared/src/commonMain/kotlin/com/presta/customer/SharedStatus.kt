package com.presta.customer

import kotlinx.coroutines.flow.MutableStateFlow

expect class SharedStatus {
    val current: MutableStateFlow<Boolean>
    fun start()
    fun stop()
}