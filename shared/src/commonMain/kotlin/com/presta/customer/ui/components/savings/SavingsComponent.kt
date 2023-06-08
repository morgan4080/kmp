package com.presta.customer.ui.components.savings

import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.savings.store.SavingsStore
import kotlinx.coroutines.flow.StateFlow

interface SavingsComponent {
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    val savingsStore: SavingsStore
    val savingsState: StateFlow<SavingsStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onEvent(event: SavingsStore.Intent)
    fun onAddSavingsSelected(sharePrice: Double)
    fun onSeeALlSelected()
    fun onBack()
}