package com.presta.customer.ui.components.registration

import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.registration.store.RegistrationStore
import com.presta.customer.ui.components.root.DefaultRootComponent
import kotlinx.coroutines.flow.StateFlow

interface RegistrationComponent {
    val authStore: AuthStore

    val registrationStore: RegistrationStore

    val state: StateFlow<RegistrationStore.State>

    val authState: StateFlow<AuthStore.State>

    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: RegistrationStore.Intent)
    fun navigate(memberRefId: String, phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext, pinStatus: PinStatus?)
}