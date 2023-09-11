package com.presta.customer

import kotlinx.coroutines.flow.MutableStateFlow

enum class Durations {
    SHORT, LONG
}

data class Contact(
    val name: String,
    val phoneNumber: String
)

expect class Platform(context: AppContext) {
    val platformName: String

    val otpCode: MutableStateFlow<String>
    fun showToast(text: String, duration: Durations = Durations.LONG)
    fun startSmsRetriever()
    fun getAppSignatures(): String
    fun openUrl(url: String)
    fun logErrorsToFirebase(Error: Exception)
    fun getContact(): List<Contact>

    //fun requestReadContactsPermission(callback: (Boolean) -> Unit)
}

