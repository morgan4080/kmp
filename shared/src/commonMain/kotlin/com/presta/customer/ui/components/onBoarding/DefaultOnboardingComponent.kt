package com.presta.customer.ui.components.onBoarding

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import com.presta.customer.organisation.OrganisationModel
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStoreFactory
import com.presta.customer.ui.components.root.DefaultRootComponent

class DefaultOnboardingComponent (
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    onBoardingContext: DefaultRootComponent.OnBoardingContext,
    private val onPush: (phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean) -> Unit,
): OnBoardingComponent, ComponentContext by componentContext {

    // after getting member data do onPush

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

    override val onBoardingStore =
        instanceKeeper.getStore {
            OnBoardingStoreFactory(
                storeFactory = storeFactory,
                onBoardingContext = onBoardingContext
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<OnBoardingStore.State> = onBoardingStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onEvent(event: OnBoardingStore.Intent) {
        onBoardingStore.accept(event)
    }

    override fun navigate(phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean) {
        onPush(
            phoneNumber,
            isTermsAccepted,
            isActive
        )
    }

    init {
        onAuthEvent(AuthStore.Intent.AuthenticateClient(
            client_secret = OrganisationModel.organisation.client_secret
        ))
    }
}