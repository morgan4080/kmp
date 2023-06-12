package com.presta.customer.ui.components.processLoanDisbursement.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.loanRequest.model.PrestaLoanPollingResponse
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
        data class LoanRequestPollingLoaded(val applicationStatus: PrestaLoanPollingResponse): Msg()
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
                is ProcessingLoanDisbursementStore.Intent.UpdateLoanRequestStatus -> dispatch(Msg.LoanRequestPollingLoaded(applicationStatus = intent.loanRequestStatus))
                is ProcessingLoanDisbursementStore.Intent.UpdateError -> dispatch(Msg.ClearError(intent.error))
                is ProcessingLoanDisbursementStore.Intent.UpdateLoading -> dispatch(Msg.ProcessingTransactionLoading(intent.isLoading))
            }
    }

    private object ReducerImpl: Reducer<ProcessingLoanDisbursementStore.State, Msg> {
        override fun ProcessingLoanDisbursementStore.State.reduce(msg: Msg): ProcessingLoanDisbursementStore.State =
            when (msg) {
                is Msg.LoanRequestPollingLoaded -> copy(loanDisburseMentStatus = msg.applicationStatus)
                is Msg.ProcessingTransactionLoading -> copy(isLoading = msg.isLoading)
                is Msg.ProcessingTransactionFailed -> copy(error = msg.error)
                is Msg.ClearError -> copy(error = msg.error)
            }
    }

}