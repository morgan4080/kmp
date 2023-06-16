package com.presta.customer.ui.components.processLoanDisbursement.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.loanRequest.model.AccountingStatuses
import com.presta.customer.network.loanRequest.model.AppraisalStatus
import com.presta.customer.network.loanRequest.model.DisbursementStatus
import com.presta.customer.network.loanRequest.model.LoanApplicationStatus
import com.presta.customer.prestaDispatchers

class ProcessLoanDisbursementStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create():ProcessingLoanDisbursementStore =
        object: ProcessingLoanDisbursementStore, Store<ProcessingLoanDisbursementStore.Intent, ProcessingLoanDisbursementStore.State, Nothing> by storeFactory.create(
            name = "ProcessingLoanRequestStore",
            initialState = ProcessingLoanDisbursementStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class LoanRequestPollingLoaded(val applicationStatus: LoanApplicationStatus?): Msg()
        data class LoanDisbursementLoaded(val disbursementStatus: DisbursementStatus?): Msg()
        data class LoanDisbursementTransactionLoaded(val transactionId: String?): Msg()
        data class LoanAppraisalLoaded(val appraisalStatus: AppraisalStatus?): Msg()
        data class LoanAccountingLoaded(val accountingStatus: AccountingStatuses?): Msg()
        data class ProcessingTransactionLoading(val isLoading: Boolean = true): Msg()
        data class ProcessingTransactionFailed(val error: String?): Msg()
        data class  ClearError(val error: String?): Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<ProcessingLoanDisbursementStore.Intent, Unit, ProcessingLoanDisbursementStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {

        override fun executeAction(action: Unit, getState: () -> ProcessingLoanDisbursementStore.State) {

        }

        override fun executeIntent(intent: ProcessingLoanDisbursementStore.Intent, getState: () -> ProcessingLoanDisbursementStore.State): Unit =
            when(intent) {
                is ProcessingLoanDisbursementStore.Intent.UpdateApprovalStatus -> dispatch(Msg.LoanRequestPollingLoaded(applicationStatus = intent.loanApprovalStatus))
                is ProcessingLoanDisbursementStore.Intent.UpdateAppraisalStatus -> dispatch(Msg.LoanAppraisalLoaded(appraisalStatus = intent.appraisalStatus))
                is ProcessingLoanDisbursementStore.Intent.UpdateDisbursementStatus -> dispatch(Msg.LoanDisbursementLoaded(disbursementStatus = intent.disbursementStatus))
                is ProcessingLoanDisbursementStore.Intent.UpdateDisbursementTransactionId -> dispatch(Msg.LoanDisbursementTransactionLoaded(transactionId = intent.transactionId))
                is ProcessingLoanDisbursementStore.Intent.UpdateAccountingStatus -> dispatch(Msg.LoanAccountingLoaded(accountingStatus = intent.accountingStatus))
                is ProcessingLoanDisbursementStore.Intent.UpdateError -> dispatch(Msg.ClearError(intent.error))
                is ProcessingLoanDisbursementStore.Intent.UpdateLoading -> dispatch(Msg.ProcessingTransactionLoading(intent.isLoading))
            }
    }

    private object ReducerImpl: Reducer<ProcessingLoanDisbursementStore.State, Msg> {
        override fun ProcessingLoanDisbursementStore.State.reduce(msg: Msg): ProcessingLoanDisbursementStore.State =
            when (msg) {
                is Msg.LoanRequestPollingLoaded -> copy(loanApplicationStatus = msg.applicationStatus)
                is Msg.LoanDisbursementLoaded -> copy(loanDisbursementStatus = msg.disbursementStatus)
                is Msg.LoanDisbursementTransactionLoaded -> copy(transactionId = msg.transactionId)
                is Msg.LoanAppraisalLoaded -> copy(loanAppraisalStatus = msg.appraisalStatus)
                is Msg.LoanAccountingLoaded -> copy(loanAccountingStatus = msg.accountingStatus)
                is Msg.ProcessingTransactionLoading -> copy(isLoading = msg.isLoading)
                is Msg.ProcessingTransactionFailed -> copy(error = msg.error)
                is Msg.ClearError -> copy(error = msg.error)
            }
    }

}