package com.presta.customer.ui.components.modeofDisbursement.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.loanRequest.model.DisbursementMethod
import com.presta.customer.network.loanRequest.model.LoanQuotationResponse
import com.presta.customer.network.loanRequest.model.LoanType

interface ModeOfDisbursementStore : Store<ModeOfDisbursementStore.Intent, ModeOfDisbursementStore.State, Nothing> {
    sealed class Intent {
        data class RequestLoan(
            val token: String,
            val amount: Int,
            val currentTerm: Boolean,
            val customerRefId: String,
            val disbursementAccountReference: String,
            val disbursementMethod: DisbursementMethod,
            val loanPeriod: Int,
            val loanType: LoanType,
            val productRefId: String,
            val referencedLoanRefId: String?,
            val requestId: String?,
            val sessionId: String,
        ): Intent()

        data class GetLoanQuotation(
            val token: String,
            val amount: Int,
            val currentTerm: Boolean,
            val customerRefId: String,
            val disbursementAccountReference: String,
            val disbursementMethod: DisbursementMethod,
            val loanPeriod: Int,
            val loanType: LoanType,
            val productRefId: String,
            val referencedLoanRefId: String?,
            val requestId: String?,
            val sessionId: String,
        ): Intent()

    }
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val requestId: String? = null,
        val prestaLoanQuotation: LoanQuotationResponse? = null
    )
}