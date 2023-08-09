package com.presta.customer.ui.components.longTermLoanApplicationStatus

import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.processingTransaction.store.ProcessingTransactionStore
import kotlinx.coroutines.flow.StateFlow

interface LongtermLoansApplicationStatusComponent {
    val correlationId: String
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    val processingTransactionStore: ProcessingTransactionStore
    val processingTransactionState: StateFlow<ProcessingTransactionStore.State>
    val amount: Double
    fun onAuthEvent(event: AuthStore.Intent)
    fun onProcessingTransactionEvent(event: ProcessingTransactionStore.Intent)
    fun retryTransaction()
    fun navigateBack()
}