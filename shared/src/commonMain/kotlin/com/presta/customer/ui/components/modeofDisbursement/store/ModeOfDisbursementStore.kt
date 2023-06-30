package com.presta.customer.ui.components.modeofDisbursement.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.loanRequest.model.DisbursementMethod
import com.presta.customer.network.loanRequest.model.LoanApplicationStatus
import com.presta.customer.network.loanRequest.model.LoanQuotationResponse
import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.network.loanRequest.model.PrestaBanksResponse
import com.presta.customer.network.loanRequest.model.PrestaCustomerBankCreatedResponse
import com.presta.customer.network.loanRequest.model.PrestaCustomerBanksResponse
import com.presta.customer.network.loanRequest.model.PrestaLoanApplicationStatusResponse

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

        data class GetPendingApprovals(
            val token: String,
            val customerRefId: String,
            val applicationStatus: List<LoanApplicationStatus>
        ): Intent()

        data class GetAllBanks(
            val token: String
        ): Intent()

        data class GetCustomerBanks(val token: String, val customerRefId: String): Intent()
        data class CreateCustomerBanks(
            val token: String,
            val customerRefId: String,
            val accountNumber: String,
            val accountName: String,
            val paybillName: String,
            val paybillNumber: String
        ): Intent()
        data class DeleteCustomerBank(
            val token: String,
            val bankAccountRefId: String
        ): Intent()

        object ClearCreatedResponse: Intent()
    }
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val requestId: String? = null,
        val prestaLoanQuotation: LoanQuotationResponse? = null,
        val loans: List<PrestaLoanApplicationStatusResponse> = listOf(),
        val banks: List<PrestaBanksResponse> = listOf(),
        val customerBanks: List<PrestaCustomerBanksResponse> = listOf(),
        val customerBankCreatedResponse: PrestaCustomerBankCreatedResponse? = null,
        val customerBankDeletedResponse: String? = null
    )
}