package components.otp.store

import com.arkivanov.mvikotlin.core.store.Store
import components.otp.OtpComponent
import components.root.DefaultRootComponent
import network.otp.model.OtpRequestResponse
import network.otp.model.OtpVerificationResponse

data class InputMethod(val value: String)

interface OtpStore: Store<OtpStore.Intent, OtpStore.State, Nothing> {
    sealed class Intent {
        data class RequestOTP(val token: String, val phoneNumber: String): Intent()
        data class VerifyOTP(val token: String, val requestMapper: String, val otp: String): Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val otpRequestData: OtpRequestResponse? = null,
        val otpVerificationData: OtpVerificationResponse? = null,
        val inputs: List<InputMethod> = listOf(
            InputMethod(
                value = ""
            ),
            InputMethod(
                value = ""
            ),
            InputMethod(
                value = ""
            ),
            InputMethod(
                value = ""
            )
        ),
        val label: String = "Please enter the code sent to",
        val title: String = "Enter OTP Verification code",
        val phone_number: String? = null,
        val email: String? = null,
        val tenant_id: String? = null,
        val onBoardingContext: DefaultRootComponent.OnBoardingContext = DefaultRootComponent.OnBoardingContext.LOGIN,
    )
}