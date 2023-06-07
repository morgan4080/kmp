package com.presta.customer.ui.components.payLoanPropmpt

import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.processingTransaction.store.ProcessingTransactionStore
import kotlinx.coroutines.flow.StateFlow

interface PayLoanPromptComponent {
    val authStore: AuthStore
    val authState: StateFlow<AuthStore.State>
    val processingTransactionStore: ProcessingTransactionStore
    val processingTransactionState: StateFlow<ProcessingTransactionStore.State>
    val amount: String
    val correlationId: String
    fun onAuthEvent(event: AuthStore.Intent)
    fun onProcessingTransactionEvent(event: ProcessingTransactionStore.Intent)
}