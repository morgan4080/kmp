package com.presta.customer.ui.components.otp

import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.otp.store.OtpStore
import com.presta.customer.ui.components.root.DefaultRootComponent
import kotlinx.coroutines.flow.StateFlow

interface OtpComponent {
    val authStore: AuthStore

    val otpStore: OtpStore

    val authState: StateFlow<AuthStore.State>

    val state: StateFlow<OtpStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: OtpStore.Intent)
    fun navigate(phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext)
}