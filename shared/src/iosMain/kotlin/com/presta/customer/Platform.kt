package com.presta.customer

import kotlinx.coroutines.flow.MutableStateFlow

actual class Platform {

    actual val otpCode = MutableStateFlow("")
    actual fun showToast(text: String, duration: Durations) {

    }
    actual fun startSmsRetriever() {}
    actual fun getAppSignatures(): String {
        return "IOS24520"
    }
    actual fun openUrl(url: String) {
        
    }

    actual fun logErrorsToFirebase(Error: Exception) {
    }
}

actual fun getPlatformName(): String = "iOS"