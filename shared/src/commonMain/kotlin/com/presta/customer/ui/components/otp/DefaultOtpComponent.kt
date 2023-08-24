package com.presta.customer.ui.components.otp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.Platform
import com.presta.customer.network.onBoarding.model.PinStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.otp.store.OtpStore
import com.presta.customer.ui.components.otp.store.OtpStoreFactory
import com.presta.customer.ui.components.root.DefaultRootComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DefaultOtpComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    onBoardingContext: DefaultRootComponent.OnBoardingContext,
    phoneNumber: String,
    memberRefId: String?,
    isTermsAccepted: Boolean,
    isActive: Boolean,
    pinStatus: PinStatus?,
    private val onValidOTP: (memberRefId: String?, phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext, pinStatus: PinStatus?) -> Unit
): OtpComponent, ComponentContext by componentContext, KoinComponent {
    override val platform by inject<Platform>()

    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                componentContext = componentContext,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false,
                pinStatus = pinStatus
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override val otpStore =
        instanceKeeper.getStore {
            OtpStoreFactory(
                storeFactory = storeFactory,
                memberRefId = memberRefId,
                onBoardingContext = onBoardingContext,
                phoneNumber = phoneNumber,
                isActive = isActive,
                pinStatus = pinStatus,
                isTermsAccepted = isTermsAccepted
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<OtpStore.State> = otpStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onEvent(event: OtpStore.Intent) {
        otpStore.accept(event)
    }

    override fun navigate(memberRefId: String?,phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext, pinStatus: PinStatus?) {
        onValidOTP(memberRefId, phoneNumber, isTermsAccepted, isActive, onBoardingContext, pinStatus)
    }

    init {
        try {
            platform.startSmsRetriever()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}