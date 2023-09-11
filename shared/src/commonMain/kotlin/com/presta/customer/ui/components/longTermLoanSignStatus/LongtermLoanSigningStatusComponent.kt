package com.presta.customer.ui.components.longTermLoanSignStatus

import com.presta.customer.ui.components.auth.store.AuthStore
import kotlinx.coroutines.flow.StateFlow

interface LongtermLoanSigningStatusComponent {
    val correlationId: String
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
   val  loanNumber: String
    val amount: Double
    fun onAuthEvent(event: AuthStore.Intent)
    fun navigateToProfile()
}