package com.presta.customer

import kotlinx.coroutines.flow.MutableStateFlow

actual open class Platform {

    actual val otpCode = MutableStateFlow("")

    actual val networkError = MutableStateFlow(false)

    actual val resultFromContact: MutableStateFlow<Map<String, String>> = MutableStateFlow(emptyMap())
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

    actual fun getContact(contactRequestCode: Int, alpha2Code: String): MutableStateFlow<Map<String, String>> {
        return resultFromContact
    }
}

actual fun getPlatformName(): String = "iOS"