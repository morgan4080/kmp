package com.presta.customer.ui.components.signAppHome

import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import kotlinx.coroutines.flow.StateFlow

interface SignHomeComponent {
    fun onApplyLoanSelected()
    fun guarantorshipRequestsSelected()
    fun favouriteGuarantorsSelected()
    fun witnessRequestSelected()
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: SignHomeStore.Intent)
    val  sigHomeStore: SignHomeStore
    val signHomeState: StateFlow<SignHomeStore.State>

}
