package com.presta.customer.ui.components.applyLoan

import com.presta.customer.ui.components.auth.store.AuthStore
import kotlinx.coroutines.flow.StateFlow

interface ApplyLoanComponent {
    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>
    fun onShortTermSelected()
    fun onLongTermSelected()
    fun onBackNavSelected()
    fun onBack()
    fun onAuthEvent(event: AuthStore.Intent)
}