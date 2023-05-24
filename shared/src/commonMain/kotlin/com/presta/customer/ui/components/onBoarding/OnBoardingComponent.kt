package com.presta.customer.ui.components.onBoarding


import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import kotlinx.coroutines.flow.StateFlow


interface OnBoardingComponent {

    val authStore: AuthStore

    val onBoardingStore: OnBoardingStore

    val authState: StateFlow<AuthStore.State>

    val state: StateFlow<OnBoardingStore.State>

    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: OnBoardingStore.Intent)
    fun navigate(phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean)
}