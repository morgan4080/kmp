package com.presta.customer.ui.components.registration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.components.registration.store.RegistrationStore
import com.presta.customer.ui.components.registration.store.RegistrationStoreFactory
import com.presta.customer.ui.components.root.DefaultRootComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultRegistrationComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    phoneNumber: String,
    isTermsAccepted: Boolean,
    isActive: Boolean,
    onBoardingContext: DefaultRootComponent.OnBoardingContext,
    private val onRegistered: (
        memberRefId: String,
        phoneNumber: String,
        isTermsAccepted: Boolean,
        isActive: Boolean,
        onBoardingContext: DefaultRootComponent.OnBoardingContext
    ) -> Unit
): RegistrationComponent, ComponentContext by componentContext {
    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                phoneNumber = phoneNumber,
                isTermsAccepted = isTermsAccepted,
                isActive = isActive
            ).create()
        }

    override val registrationStore =
        instanceKeeper.getStore {
            RegistrationStoreFactory(
                storeFactory = storeFactory,
                phoneNumber = phoneNumber,
                isTermsAccepted = isTermsAccepted,
                isActive = isActive,
                onBoardingContext = onBoardingContext
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<RegistrationStore.State> = registrationStore.stateFlow
    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow
    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onEvent(event: RegistrationStore.Intent) {
        registrationStore.accept(event)
    }

    override fun navigate(
        memberRefId: String,
        phoneNumber: String,
        isTermsAccepted: Boolean,
        isActive: Boolean,
        onBoardingContext: DefaultRootComponent.OnBoardingContext
    ) {
        onRegistered(
            memberRefId,
            phoneNumber,
            isTermsAccepted,
            isActive,
            onBoardingContext
        )
    }
}