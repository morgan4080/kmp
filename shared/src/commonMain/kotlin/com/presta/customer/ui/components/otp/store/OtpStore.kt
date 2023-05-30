package com.presta.customer.ui.components.otp.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.network.otp.model.OtpRequestResponse
import com.presta.customer.network.otp.model.OtpVerificationResponse
import com.presta.customer.ui.components.root.DefaultRootComponent

data class InputMethod(val value: String)

interface OtpStore: Store<OtpStore.Intent, OtpStore.State, Nothing> {
    sealed class Intent {
        data class RequestOTP(val token: String, val phoneNumber: String, val tenantId: String): Intent()
        data class VerifyOTP(val token: String, val requestMapper: String, val otp: String, val tenantId: String): Intent()
        object ClearOtpVerificationData: Intent()
        object ClearError: Intent()
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
        val isActive: Boolean? = null,
        val isTermsAccepted: Boolean? = null,
        val memberRefId: String? = null,
        val pinStatus: PinStatus? = null,
        val email: String? = null,
        val tenant_id: String? = null,
        val onBoardingContext: DefaultRootComponent.OnBoardingContext = DefaultRootComponent.OnBoardingContext.LOGIN,
    )
}