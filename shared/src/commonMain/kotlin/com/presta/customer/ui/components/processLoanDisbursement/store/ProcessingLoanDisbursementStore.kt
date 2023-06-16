package com.presta.customer.ui.components.processLoanDisbursement.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.loanRequest.model.AccountingStatuses
import com.presta.customer.network.loanRequest.model.AppraisalStatus
import com.presta.customer.network.loanRequest.model.DisbursementStatus
import com.presta.customer.network.loanRequest.model.LoanApplicationStatus

interface ProcessingLoanDisbursementStore : Store<ProcessingLoanDisbursementStore.Intent, ProcessingLoanDisbursementStore.State, Nothing> {

    sealed class Intent {
        data class UpdateApprovalStatus(val loanApprovalStatus: LoanApplicationStatus?): Intent()
        data class UpdateDisbursementStatus(val disbursementStatus: DisbursementStatus?): Intent()
        data class UpdateDisbursementTransactionId(val transactionId: String?): Intent()
        data class UpdateAppraisalStatus(val appraisalStatus: AppraisalStatus?): Intent()
        data class UpdateAccountingStatus(val accountingStatus: AccountingStatuses?): Intent()
        data class UpdateError(val error: String?): Intent()
        data class UpdateLoading(val isLoading: Boolean): Intent()
    }
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val loanApplicationStatus: LoanApplicationStatus? = null,
        val loanAppraisalStatus: AppraisalStatus? = null,
        val loanDisbursementStatus: DisbursementStatus? = null,
        val loanAccountingStatus: AccountingStatuses? = null,
        val transactionId: String? = null
    )

}