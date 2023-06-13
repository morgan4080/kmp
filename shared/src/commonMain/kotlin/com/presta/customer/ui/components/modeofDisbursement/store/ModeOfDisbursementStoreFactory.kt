package com.presta.customer.ui.components.modeofDisbursement.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.loanRequest.data.LoanRequestRepository
import com.presta.customer.network.loanRequest.model.DisbursementMethod
import com.presta.customer.network.loanRequest.model.LoanType
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
                    dispatch(Msg.LoanRequestLoaded(response))
                    println("Request processed::::::"+ response)


                }.onFailure { e ->
                    dispatch(Msg.LoanRequestFailedFailed(e.message))
                }
                dispatch(Msg.LoanRequestsLoading(false))
            }
        }

    }
    private object ReducerImpl : Reducer<ModeOfDisbursementStore.State, Msg> {
        override fun ModeOfDisbursementStore.State.reduce(msg: Msg): ModeOfDisbursementStore.State =
            when (msg) {
                is Msg.LoanRequestLoaded -> copy(requestId = msg.requestId)
                is Msg.LoanRequestsLoading -> copy(isLoading = msg.isLoading)
                is Msg.LoanRequestFailedFailed -> copy(error = msg.error)
            }
    }
}