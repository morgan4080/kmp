package com.presta.customer.ui.components.onBoarding


import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import com.presta.customer.ui.components.root.DefaultRootComponent
import kotlinx.coroutines.flow.StateFlow


interface OnBoardingComponent {

    val onBoardingStore: OnBoardingStore

    val state: StateFlow<OnBoardingStore.State>
    fun onEvent(event: OnBoardingStore.Intent)
    fun navigate(memberRefId: String?, phoneNumber: String, isTermsAccepted: Boolean, isActive: Boolean, onBoardingContext: DefaultRootComponent.OnBoardingContext, pinStatus: PinStatus?)
}