package components.otp

import com.arkivanov.decompose.value.Value
import components.auth.store.AuthStore
import components.onBoarding.store.OnBoardingStore
import components.otp.store.OtpStore
import kotlinx.coroutines.flow.StateFlow

interface OtpComponent {
    val authStore: AuthStore

    val onBoardingStore: OnBoardingStore

    val otpStore: OtpStore

    val authState: StateFlow<AuthStore.State>

    val onBoardingState: StateFlow<OnBoardingStore.State>

    val state: StateFlow<OtpStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onOnBoardingEvent(event: OnBoardingStore.Intent)
    fun onEvent(event: OtpStore.Intent)
    fun navigate()
}