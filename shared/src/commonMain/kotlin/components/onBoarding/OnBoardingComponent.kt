package components.onBoarding

import components.auth.store.AuthStore
import components.onBoarding.store.OnBoardingStore
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