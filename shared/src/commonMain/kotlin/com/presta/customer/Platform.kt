package com.presta.customer

import com.mohamedrejeb.calf.ui.web.WebViewState
import kotlinx.coroutines.flow.MutableStateFlow

enum class Durations {
    SHORT, LONG
}

expect class Platform {

    val otpCode: MutableStateFlow<String>
    val resultFromContact: MutableStateFlow<Map<String, String>>
    fun showToast(text: String, duration: Durations = Durations.LONG)
    fun startSmsRetriever()
    fun getAppSignatures(): String
    fun openUrl(url: String)
    fun logErrorsToFirebase(Error: Exception)

    fun getContact(contactRequestCode: Int, alpha2Code: String): MutableStateFlow<Map<String, String>>
//    fun acceptWebViewCookies(webview: WebViewState)
}

expect fun getPlatformName(): String
