package com.presta.customer.ui.components.processingTransaction.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.payments.model.PaymentStatuses

interface ProcessingTransactionStore: Store<ProcessingTransactionStore.Intent, ProcessingTransactionStore.State, Nothing> {
    sealed class Intent {
        data class UpdatePaymentStatus(val paymentStatus: PaymentStatuses): Intent()
        data class UpdateError(val error: String?): Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val paymentStatus: PaymentStatuses? = null
    )
}