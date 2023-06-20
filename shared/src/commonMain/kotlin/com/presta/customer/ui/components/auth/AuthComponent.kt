package com.presta.customer.ui.components.auth


import com.presta.customer.Platform
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.components.root.DefaultRootComponent
import kotlinx.coroutines.flow.StateFlow

interface AuthComponent {
    val platform: Platform
    val authStore: AuthStore
    val onBoardingStore: OnBoardingStore
    val state: StateFlow<AuthStore.State>
    val onBoardingState: StateFlow<OnBoardingStore.State>
    fun onEvent(event: AuthStore.Intent)
    fun onOnBoardingEvent(event: OnBoardingStore.Intent)
    fun navigate()
}