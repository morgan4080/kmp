package com.presta.customer.ui.components.modeofDisbursement.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.loanRequest.data.LoanRequestRepository
import com.presta.customer.network.loanRequest.model.DisbursementMethod
import com.presta.customer.network.loanRequest.model.LoanApplicationStatus
import com.presta.customer.network.loanRequest.model.LoanQuotationResponse
import com.presta.customer.network.loanRequest.model.LoanType
import com.presta.customer.network.loanRequest.model.PrestaBanksResponse
import com.presta.customer.network.loanRequest.model.PrestaCustomerBankCreatedResponse
import com.presta.customer.network.loanRequest.model.PrestaCustomerBankDeletedResponse
import com.presta.customer.network.loanRequest.model.PrestaCustomerBanksResponse
import com.presta.customer.network.loanRequest.model.PrestaLoanApplicationStatusResponse
import com.presta.customer.prestaDispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ModeOfDisbursementStoreFactory(
    private val storeFactory: StoreFactory
) : KoinComponent {
    private val loanRequestRepository by inject<LoanRequestRepository>()

    fun create(): ModeOfDisbursementStore =
        object : ModeOfDisbursementStore, Store<ModeOfDisbursementStore.Intent, ModeOfDisbursementStore.State, Nothing> by storeFactory.create(
                name = "ModeOfDisbursementStore",
                initialState = ModeOfDisbursementStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}
    private sealed class Msg {
        data class LoanRequestLoaded(val requestId:  String?) : Msg()

        data class LoanRequestsLoading(val isLoading: Boolean = true) : Msg()

        data class LoanRequestFailedFailed(val error: String?) : Msg()
        data class LoanQuotationDataLoaded(val loanQuotationResponse: LoanQuotationResponse ) :Msg()
        data class PendingLoanDataLoaded(val loans: List<PrestaLoanApplicationStatusResponse>) :Msg()
        data class AllBanksLoaded(val banks: List<PrestaBanksResponse>) :Msg()
        data class CustomerBanksLoaded(val customerBanks: List<PrestaCustomerBanksResponse>) :Msg()
        data class CustomerBanksDeleted(val customerBankDeletedResponse: PrestaCustomerBankDeletedResponse) :Msg()
        data class CustomerBanksCreated(val customerBankCreatedResponse: PrestaCustomerBankCreatedResponse) :Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<ModeOfDisbursementStore.Intent, Unit, ModeOfDisbursementStore.State, Msg, Nothing>(
            prestaDispatchers.main
        ) {
        override fun executeAction(action: Unit, getState: () -> ModeOfDisbursementStore.State) {

        }
        override fun executeIntent(intent: ModeOfDisbursementStore.Intent, getState: () -> ModeOfDisbursementStore.State): Unit =
            when (intent) {
                is ModeOfDisbursementStore.Intent.RequestLoan -> requestLoan (
                    token = intent.token,
                    amount = intent.amount,
                    currentTerm = intent.currentTerm,
                    customerRefId = intent.customerRefId,
                    disbursementAccountReference = intent.disbursementAccountReference,
                    disbursementMethod = intent.disbursementMethod,
                    loanPeriod = intent.loanPeriod,
                    loanType = intent.loanType,
                    productRefId = intent.productRefId,
                    referencedLoanRefId = intent.referencedLoanRefId,
                    requestId = intent.requestId,
                    sessionId = intent.sessionId
                )

                is ModeOfDisbursementStore.Intent.GetLoanQuotation-> checkLoanQuotation (
                    token = intent.token,
                    amount = intent.amount,
                    currentTerm = intent.currentTerm,
                    customerRefId = intent.customerRefId,
                    disbursementAccountReference = intent.disbursementAccountReference,
                    disbursementMethod = intent.disbursementMethod,
                    loanPeriod = intent.loanPeriod,
                    loanType = intent.loanType,
                    productRefId = intent.productRefId,
                    referencedLoanRefId = intent.referencedLoanRefId,
                    requestId = intent.requestId,
                    sessionId = intent.sessionId
                )

                is ModeOfDisbursementStore.Intent.GetPendingApprovals -> getPendingLoanApprovals(
                    token = intent.token,
                    customerRefId = intent.customerRefId,
                    applicationStatus = intent.applicationStatus
                )

                is ModeOfDisbursementStore.Intent.GetAllBanks -> getAllBanks(
                    token = intent.token
                )

                is ModeOfDisbursementStore.Intent.GetCustomerBanks -> getCustomerBanks(
                    token = intent.token,
                    customerRefId = intent.customerRefId
                )

                is ModeOfDisbursementStore.Intent.CreateCustomerBanks -> createCustomerBanks(
                    token = intent.token,
                    customerRefId = intent.customerRefId,
                    accountNumber = intent.accountNumber,
                    accountName = intent.accountName,
                    paybillName = intent.paybillName,
                    paybillNumber = intent.paybillNumber,
                )

                is ModeOfDisbursementStore.Intent.DeleteCustomerBank -> deleteCustomerBank(
                    token = intent.token,
                    bankAccountRefId = intent.bankAccountRefId
                )
            }
        private var requestLoanJob: Job? = null
        private fun requestLoan(
            token: String,
            amount: Int,
            currentTerm: Boolean,
            customerRefId: String,
            disbursementAccountReference: String,
            disbursementMethod: DisbursementMethod,
            loanPeriod: Int,
            loanType: LoanType,
            productRefId: String,
            referencedLoanRefId: String?=null,
            requestId: String?=null,
            sessionId: String
        ) {
            if (requestLoanJob?.isActive == true) return
            dispatch(Msg.LoanRequestsLoading())

            requestLoanJob = scope.launch {
                loanRequestRepository.requestLoan (
                    token,
                    amount,
                    currentTerm,
                    customerRefId,
                    disbursementAccountReference,
                    disbursementMethod,
                    loanPeriod,
                    loanType,
                    productRefId,
                    referencedLoanRefId,
                    requestId,
                    sessionId,
                ).onSuccess { response ->
                    dispatch(Msg.LoanRequestLoaded(response.requestId))
                }.onFailure { e ->
                    dispatch(Msg.LoanRequestFailedFailed(e.message))
                }
                dispatch(Msg.LoanRequestsLoading(false))
            }
        }

        private var requestLoanQuotationJob: Job? = null
        private fun checkLoanQuotation(
            token: String,
            amount: Int,
            currentTerm: Boolean,
            customerRefId: String,
            disbursementAccountReference: String,
            disbursementMethod: DisbursementMethod,
            loanPeriod: Int,
            loanType: LoanType,
            productRefId: String,
            referencedLoanRefId: String?=null,
            requestId: String?=null,
            sessionId: String
        ) {
            if (requestLoanQuotationJob?.isActive == true) return
            dispatch(Msg.LoanRequestsLoading())

            requestLoanQuotationJob= scope.launch {
                loanRequestRepository.loanQuotationRequest (
                    token,
                    amount,
                    currentTerm,
                    customerRefId,
                    disbursementAccountReference,
                    disbursementMethod,
                    loanPeriod,
                    loanType,
                    productRefId,
                    referencedLoanRefId,
                    requestId,
                    sessionId,
                ).onSuccess { response ->
                    dispatch(Msg.LoanQuotationDataLoaded(response))
                    dispatch(Msg.LoanRequestsLoading(false))
                }.onFailure { e ->
                    dispatch(Msg.LoanRequestFailedFailed(e.message))
                    dispatch(Msg.LoanRequestsLoading(false))
                }
            }
        }

        private var pendingLoanApprovalsJob: Job? = null
        private fun getPendingLoanApprovals(
            token: String,
            customerRefId: String,
            applicationStatus: List<LoanApplicationStatus>
        ) {
            if (pendingLoanApprovalsJob?.isActive == true) return
            dispatch(Msg.LoanRequestsLoading(true))

            pendingLoanApprovalsJob = scope.launch {
                loanRequestRepository.getPendingLoans(
                    token, customerRefId, applicationStatus
                ).onSuccess { response ->
                    dispatch(Msg.PendingLoanDataLoaded(response))
                    dispatch(Msg.LoanRequestsLoading(false))
                }.onFailure { e ->
                    dispatch(Msg.LoanRequestFailedFailed(e.message))
                    dispatch(Msg.LoanRequestsLoading(false))
                }
            }
        }
        private var getAllBanksJob: Job? = null
        private fun getAllBanks(
            token: String
        ) {
            if (getAllBanksJob?.isActive == true) return
            dispatch(Msg.LoanRequestsLoading(true))

            getAllBanksJob = scope.launch {
                loanRequestRepository.getAllBanks(
                    token
                ).onSuccess { response ->
                    dispatch(Msg.AllBanksLoaded(response))
                    dispatch(Msg.LoanRequestsLoading(false))
                }.onFailure { e ->
                    dispatch(Msg.LoanRequestFailedFailed(e.message))
                    dispatch(Msg.LoanRequestsLoading(false))
                }
            }
        }
        private var getCustomerBanksJob: Job? = null
        private fun getCustomerBanks(
            token: String,
            customerRefId: String
        ) {
            if (getCustomerBanksJob?.isActive == true) return
            dispatch(Msg.LoanRequestsLoading(true))

            getCustomerBanksJob = scope.launch {
                loanRequestRepository.getCustomerBanks(
                    token,
                    customerRefId
                ).onSuccess { response ->
                    dispatch(Msg.CustomerBanksLoaded(response))
                    dispatch(Msg.LoanRequestsLoading(false))
                }.onFailure { e ->
                    dispatch(Msg.LoanRequestFailedFailed(e.message))
                    dispatch(Msg.LoanRequestsLoading(false))
                }
            }
        }
        private var createCustomerBanksJob: Job? = null
        private fun createCustomerBanks(
            token: String,
            customerRefId: String,
            accountNumber: String,
            accountName: String,
            paybillName: String,
            paybillNumber: String
        ) {
            if (createCustomerBanksJob?.isActive == true) return
            dispatch(Msg.LoanRequestsLoading(true))

            createCustomerBanksJob = scope.launch {
                loanRequestRepository.createCustomerBanks(
                    token,
                    customerRefId,
                    accountNumber,
                    accountName,
                    paybillName,
                    paybillNumber
                ).onSuccess { response ->
                    dispatch(Msg.CustomerBanksCreated(response))
                    dispatch(Msg.LoanRequestsLoading(false))
                }.onFailure { e ->
                    dispatch(Msg.LoanRequestFailedFailed(e.message))
                    dispatch(Msg.LoanRequestsLoading(false))
                }
            }
        }
        private var deleteCustomerBanksJob: Job? = null
        private fun deleteCustomerBank(
            token: String,
            bankAccountRefId: String
        ) {
            if (deleteCustomerBanksJob?.isActive == true) return
            dispatch(Msg.LoanRequestsLoading(true))

            deleteCustomerBanksJob = scope.launch {
                loanRequestRepository.deleteCustomerBank(
                    token,
                    bankAccountRefId
                ).onSuccess { response ->
                    dispatch(Msg.CustomerBanksDeleted(response))
                    dispatch(Msg.LoanRequestsLoading(false))
                }.onFailure { e ->
                    dispatch(Msg.LoanRequestFailedFailed(e.message))
                    dispatch(Msg.LoanRequestsLoading(false))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<ModeOfDisbursementStore.State, Msg> {
        override fun ModeOfDisbursementStore.State.reduce(msg: Msg): ModeOfDisbursementStore.State =
            when (msg) {
                is Msg.LoanRequestLoaded -> copy(requestId = msg.requestId)
                is Msg.LoanQuotationDataLoaded-> copy  (prestaLoanQuotation = msg.loanQuotationResponse)
                is Msg.PendingLoanDataLoaded-> copy  (loans = msg.loans)
                is Msg.LoanRequestsLoading -> copy(isLoading = msg.isLoading)
                is Msg.LoanRequestFailedFailed -> copy(error = msg.error)
                is Msg.AllBanksLoaded -> copy(banks = msg.banks)
                is Msg.CustomerBanksLoaded -> copy(customerBanks = msg.customerBanks)
                is Msg.CustomerBanksDeleted -> copy(customerBankDeletedResponse = msg.customerBankDeletedResponse)
                is Msg.CustomerBanksCreated -> copy(customerBankCreatedResponse = msg.customerBankCreatedResponse)
            }
    }
}