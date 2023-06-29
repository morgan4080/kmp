package com.presta.customer.ui.components.onBoarding

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.Platform
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStoreFactory
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.components.tenant.store.TenantStore
import com.presta.customer.ui.components.tenant.store.TenantStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DefaultOnboardingComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    //mainContext: CoroutineContext,
    onBoardingContext: DefaultRootComponent.OnBoardingContext,
    val tenantId: String?,
    private val onPush: (memberRefId: String?, phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext, pinStatus: PinStatus?) -> Unit,
) : OnBoardingComponent, ComponentContext by componentContext,KoinComponent {
    override val platform by inject<Platform>()
    override val onBoardingStore =
        instanceKeeper.getStore {
            OnBoardingStoreFactory(
                storeFactory = storeFactory,
                onBoardingContext = onBoardingContext
            ).create()
        }

    override val tenantStore: TenantStore =
        instanceKeeper.getStore {
            TenantStoreFactory(
                storeFactory = storeFactory,
                componentContext = componentContext,
            ).create()
        }

    //private val scope = coroutineScope(mainContext + SupervisorJob())

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<OnBoardingStore.State> = onBoardingStore.stateFlow

    override fun onEvent(event: OnBoardingStore.Intent) {
        onBoardingStore.accept(event)
    }

    override fun navigate(
        memberRefId: String?,
        phoneNumber: String,
        isTermsAccepted: Boolean,
        isActive: Boolean,
        onBoardingContext: DefaultRootComponent.OnBoardingContext,
        pinStatus: PinStatus?
    ) {
        onPush(
            memberRefId,
            phoneNumber,
            isTermsAccepted,
            isActive,
            onBoardingContext,
            pinStatus
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val tenantState: StateFlow<TenantStore.State> = tenantStore.stateFlow

    override fun onTenantEvent(event: TenantStore.Intent) {
        tenantStore.accept(event)
    }

    init {

        if (tenantId !== null) {
            // call https://lending.presta.co.ke/applications/api/v2/tenants-query/search?searchTerm=t10007
            onTenantEvent(
                TenantStore.Intent.GetClientById(
                    searchTerm = tenantId
                )
            )
        }
    }
}