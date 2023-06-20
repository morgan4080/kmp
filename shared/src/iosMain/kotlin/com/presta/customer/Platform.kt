package com.presta.customer

import kotlinx.coroutines.flow.MutableStateFlow

enum class Durations {
    SHORT, LONG
}

actual class Platform actual constructor(context: AppContext) {
    actual val platformName: String = "iOS"

    actual val otpCode = MutableStateFlow("")
    actual fun showToast(text: String, duration: Durations) {}
    actual fun startSmsRetriever() {}
    actual fun getAppSignatures(): String {
        return "IOS24520"
    }
}