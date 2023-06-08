package com.presta.customer.ui.components.addSavings

import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.ui.components.addSavings.store.AddSavingsStore
import com.presta.customer.ui.components.auth.store.AuthStore
import kotlinx.coroutines.flow.StateFlow

interface AddSavingsComponent {
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>

    val addSavingsStore: AddSavingsStore
    val addSavingsState: StateFlow<AddSavingsStore.State>
    val sharePrice: Double
    fun onConfirmSelected(mode: PaymentTypes, amount: Double)
    fun onBackNavSelected()
    fun onAuthEvent(event: AuthStore.Intent)
    fun onAddSavingsEvent(event: AddSavingsStore.Intent)
}