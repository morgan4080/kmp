package com.presta.customer.ui.components.onBoarding


import com.presta.customer.Platform
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.components.tenant.store.TenantStore
import kotlinx.coroutines.flow.StateFlow


interface OnBoardingComponent {
    val platform: Platform
    val onBoardingStore: OnBoardingStore

    val state: StateFlow<OnBoardingStore.State>
    fun onEvent(event: OnBoardingStore.Intent)
    val tenantStore : TenantStore

    val tenantState: StateFlow<TenantStore.State>
    fun onTenantEvent(event: TenantStore.Intent)
    fun navigate(memberRefId: String?, phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext, pinStatus: PinStatus?)
}