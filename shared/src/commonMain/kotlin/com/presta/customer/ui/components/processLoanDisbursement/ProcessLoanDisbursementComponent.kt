package com.presta.customer.ui.components.processLoanDisbursement

import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.processLoanDisbursement.store.ProcessingLoanDisbursementStore
import kotlinx.coroutines.flow.StateFlow

interface ProcessLoanDisbursementComponent {
    val authState: StateFlow<AuthStore.State>
    val authStore: AuthStore
    val amount: Double
    val fees: Double
    val requestId: String
    val processingLoanDisbursementStore: ProcessingLoanDisbursementStore
    val processingTransactionState: StateFlow<ProcessingLoanDisbursementStore.State>
    fun onAuthEvent(event: AuthStore.Intent)
    fun onProcessingLoanDisbursementEvent(event: ProcessingLoanDisbursementStore.Intent)
    fun navigate()
}