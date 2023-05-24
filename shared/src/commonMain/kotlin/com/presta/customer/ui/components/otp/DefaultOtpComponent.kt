package com.presta.customer.ui.components.otp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.otp.store.OtpStore
import com.presta.customer.ui.components.otp.store.OtpStoreFactory
import com.presta.customer.ui.components.root.DefaultRootComponent

class DefaultOtpComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    onBoardingContext: DefaultRootComponent.OnBoardingContext,
    phoneNumber: String,
    isTermsAccepted: Boolean,
    isActive: Boolean,
    private val onValidOTP: (phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext) -> Unit
): OtpComponent, ComponentContext by componentContext {
    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override val otpStore =
        instanceKeeper.getStore {
            OtpStoreFactory(
                storeFactory = storeFactory,
                onBoardingContext = onBoardingContext,
                phoneNumber = phoneNumber,
                isActive = isActive,
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

    override fun navigate(phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext) {
        onValidOTP(phoneNumber, isTermsAccepted, isActive, onBoardingContext)
    }

    init {
        onAuthEvent(AuthStore.Intent.AuthenticateClient(
            client_secret = OrganisationModel.organisation.client_secret
        ))
    }
}