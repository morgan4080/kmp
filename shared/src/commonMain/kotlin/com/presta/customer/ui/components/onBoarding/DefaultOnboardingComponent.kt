package com.presta.customer.ui.components.onBoarding

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.network.onBoarding.model.PinStatus
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
    private val onPush: (memberRefId: String?, phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext, pinStatus: PinStatus?) -> Unit,
): OnBoardingComponent, ComponentContext by componentContext {
    override val onBoardingStore =
        instanceKeeper.getStore {
            OnBoardingStoreFactory(
                storeFactory = storeFactory,
                onBoardingContext = onBoardingContext
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<OnBoardingStore.State> = onBoardingStore.stateFlow

    override fun onEvent(event: OnBoardingStore.Intent) {
        onBoardingStore.accept(event)
    }

    override fun navigate(memberRefId: String?, phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext, pinStatus: PinStatus?) {
        onPush(
            memberRefId,
            phoneNumber,
            isTermsAccepted,
            isActive,
            onBoardingContext,
            pinStatus
        )
    }
}