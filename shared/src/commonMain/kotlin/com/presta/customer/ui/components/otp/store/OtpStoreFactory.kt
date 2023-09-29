package com.presta.customer.ui.components.otp.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.onBoarding.model.PinStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.presta.customer.network.otp.data.OtpRepository
import com.presta.customer.network.otp.model.OtpRequestResponse
import com.presta.customer.network.otp.model.OtpVerificationResponse
import com.presta.customer.ui.components.root.DefaultRootComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.presta.customer.prestaDispatchers

internal class OtpStoreFactory (
    private val storeFactory: StoreFactory,
    private val onBoardingContext: DefaultRootComponent.OnBoardingContext,
    private val isTermsAccepted: Boolean,
    private val isActive: Boolean,
    private val memberRefId: String?,
    private val pinStatus: PinStatus?,
    private val phoneNumber: String,
): KoinComponent {
    private val otpRepository by inject<OtpRepository>()

    fun create(): OtpStore =
        object : OtpStore, Store<OtpStore.Intent, OtpStore.State, Nothing> by storeFactory.create(
            name = "OtpStore",
            initialState = OtpStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ){}

    private sealed class Msg {
        data class OtpLoading(val isLoading: Boolean = true): Msg()
        object ClearOtpVerificationData: Msg()
        object ClearError: Msg()
        data class OtpRequestLoaded(val otpRequestData: OtpRequestResponse): Msg()
        data class OtpValidationLoaded(val otpVerificationData: OtpVerificationResponse): Msg()
        data class OtpFailed(val error: String?) : Msg()
        data class OnBoardingContext(val onBoardingContext: DefaultRootComponent.OnBoardingContext) : Msg()
        data class OtpPrimeData(val memberRefId: String?,val pinStatus: PinStatus?, val phoneNumber: String, val isActive: Boolean, val isTermsAccepted: Boolean) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<OtpStore.Intent, Unit, OtpStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {
        override fun executeAction(action: Unit, getState: () -> OtpStore.State) {
            dispatch(Msg.OnBoardingContext(onBoardingContext = onBoardingContext))
            dispatch(Msg.OtpPrimeData(
                phoneNumber = phoneNumber,
                isActive = isActive,
                isTermsAccepted = isTermsAccepted,
                memberRefId = memberRefId,
                pinStatus = pinStatus
            ))
        }

        override fun executeIntent(intent: OtpStore.Intent, getState: () -> OtpStore.State): Unit =
            when (intent) {
                is OtpStore.Intent.RequestOTP -> requestOtp(
                    token = intent.token,
                    phoneNumber = intent.phoneNumber,
                    tenantId = intent.tenantId
                )
                is OtpStore.Intent.VerifyOTP -> verifyOtp (
                    token = intent.token,
                    requestMapper = intent.requestMapper,
                    otp = intent.otp,
                    tenantId = intent.tenantId
                )
                is OtpStore.Intent.ClearOtpVerificationData -> dispatch(Msg.ClearOtpVerificationData)
                is OtpStore.Intent.ClearError -> dispatch(Msg.ClearError)
            }

        private var requestOtpJob: Job? = null

        private fun requestOtp(
            token: String,
            phoneNumber: String,
            tenantId: String
        ) {
            if (requestOtpJob?.isActive == true) return
            dispatch(Msg.OtpLoading())
            requestOtpJob = scope.launch {
                otpRepository.requestOtp(token, phoneNumber, tenantId)
                    .onSuccess {response ->
                        dispatch(Msg.OtpRequestLoaded(response))
                    }.onFailure { e ->
                        dispatch(Msg.OtpFailed(e.message))
                    }

                dispatch(Msg.OtpLoading(false))
            }
        }

        private var verifyOtpJob: Job? = null

        private fun verifyOtp (
            token: String,
            requestMapper: String,
            otp: String,
            tenantId: String
        ) {
            if (verifyOtpJob?.isActive == true) return

            dispatch(Msg.OtpLoading())

            verifyOtpJob = scope.launch {
                otpRepository.verifyOtp (
                    token,
                    requestMapper,
                    otp,
                    tenantId
                ).onSuccess {response ->
                    dispatch(Msg.OtpValidationLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.OtpFailed(e.message))
                }

                dispatch(Msg.OtpLoading(false))
            }
        }
    }

    private object ReducerImpl: Reducer<OtpStore.State, Msg> {
        override fun OtpStore.State.reduce(msg: Msg): OtpStore.State =
            when (msg) {
                is Msg.OtpLoading -> copy(isLoading = msg.isLoading)
                is Msg.OtpRequestLoaded -> copy(otpRequestData = msg.otpRequestData)
                is Msg.OtpValidationLoaded -> copy(otpVerificationData = msg.otpVerificationData)
                is Msg.OtpFailed -> copy(error = msg.error)
                is Msg.OnBoardingContext -> copy(onBoardingContext = msg.onBoardingContext)
                is Msg.OtpPrimeData -> copy(
                    phone_number = msg.phoneNumber,
                    isActive = msg.isActive,
                    isTermsAccepted = msg.isTermsAccepted,
                    memberRefId = msg.memberRefId,
                    pinStatus = msg.pinStatus
                )
                is Msg.ClearOtpVerificationData -> copy(
                    otpVerificationData = null
                )
                is Msg.ClearError -> copy(
                    error = null
                )
            }
    }
}