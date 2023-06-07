package com.presta.customer.ui.components.processLoanDisbursement.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.payments.model.PrestaPollingResponse

interface ProcessingLoanDisbursementStore : Store<ProcessingLoanDisbursementStore.Intent, ProcessingLoanDisbursementStore.State, Nothing> {

    sealed class Intent {
        data class UpdateLoanRequestStatus(val loanaRequestStatus: PrestaPollingResponse): Intent()
        data class UpdateError(val error: String?): Intent()
        data class UpdateLoading(val isLoading: Boolean): Intent()
    }
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val paymentStatus: PrestaPollingResponse? = null
    )

}