package com.presta.customer.ui.components.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.github.ln_12.library.ConnectivityStatus
import com.github.ln_12.library.AppContext
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStoreFactory
import com.presta.customer.ui.components.root.DefaultRootComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultAuthComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    phoneNumber: String,
    isTermsAccepted: Boolean,
    isActive: Boolean,
    pinStatus: PinStatus?,
    onBoardingContext: DefaultRootComponent.OnBoardingContext,
    private val onLogin: () -> Unit,
): AuthComponent, ComponentContext by componentContext {

    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                phoneNumber = phoneNumber,
                isTermsAccepted = isTermsAccepted,
                isActive = isActive,
                pinStatus = pinStatus,
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<AuthStore.State> = authStore.stateFlow

    override val onBoardingStore =
        instanceKeeper.getStore {
            OnBoardingStoreFactory(
                storeFactory = storeFactory,
                onBoardingContext = onBoardingContext
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val onBoardingState: StateFlow<OnBoardingStore.State> = onBoardingStore.stateFlow

    init {
        /*onEvent(AuthStore.Intent.AuthenticateClient(
           client_secret = OrganisationModel.organisation.client_secret
        ))*/
    }

    // authenticate client to get token
    // token gives access to member api, member api will have pin status and terms status
    // token access allows verified user to set pin on an account if terms not accepted or pinStatus != SET

    // EVENTS
    // - AuthenticateClient
    // - GetMemberDetails
    // - UpdateMember
    // - LoginUser

    override fun onEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onOnBoardingEvent(event: OnBoardingStore.Intent) {
        onBoardingStore.accept(event)
    }

    override fun navigate() {
        onLogin()
    }

    init {

    }
}