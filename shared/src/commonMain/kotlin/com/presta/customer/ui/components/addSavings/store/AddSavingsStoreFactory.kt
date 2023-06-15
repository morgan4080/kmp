package com.presta.customer.ui.components.addSavings.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.network.payments.data.PaymentsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.presta.customer.prestaDispatchers

class AddSavingsStoreFactory (
    private val storeFactory: StoreFactory
): KoinComponent {
    private val paymentsRepository by inject<PaymentsRepository>()

    fun create(): AddSavingsStore =
        object: AddSavingsStore, Store<AddSavingsStore.Intent, AddSavingsStore.State, Nothing> by storeFactory.create(
            name = "AddSavingsStore",
            initialState = AddSavingsStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}
    private sealed class Msg {
        data class AddSavingsLoaded(val correlationId: String?): Msg()
        data class AddSavingsLoading(val isLoading: Boolean = true): Msg()
        data class AddSavingsFailed(val error: String?): Msg()
        data class  ClearError(val error: String?): Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<AddSavingsStore.Intent, Unit, AddSavingsStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {

        override fun executeAction(action: Unit, getState: () -> AddSavingsStore.State) {

        }

        override fun executeIntent(intent: AddSavingsStore.Intent, getState: () -> AddSavingsStore.State): Unit =
            when(intent) {
                is AddSavingsStore.Intent.MakePayment -> makePayments (
                    token = intent.token,
                    phoneNumber = intent.phoneNumber,
                    loanRefId = intent.loanRefId,
                    beneficiaryPhoneNumber = intent.beneficiaryPhoneNumber,
                    amount = intent.amount,
                    paymentType = intent.paymentType
                )
                is AddSavingsStore.Intent.ClearCorrelationId -> dispatch(Msg.AddSavingsLoaded(intent.correlationId))
                is AddSavingsStore.Intent.UpdateError -> dispatch(Msg.ClearError(intent.error))
            }

        private var makePaymentsJob: Job? = null

        private fun makePayments(
            token: String,
            phoneNumber: String,
            loanRefId: String?,
            beneficiaryPhoneNumber: String?,
            amount: Double,
            paymentType: PaymentTypes
        ) {
            if (makePaymentsJob?.isActive == true) return

            dispatch(Msg.AddSavingsLoading())

            makePaymentsJob = scope.launch {
                paymentsRepository.makePayment (
                    token,
                    phoneNumber,
                    loanRefId,
                    beneficiaryPhoneNumber,
                    amount,
                    paymentType
                ).onSuccess { response ->
                    dispatch(Msg.AddSavingsLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.AddSavingsFailed(e.message))
                }

                dispatch(Msg.AddSavingsLoading(false))
            }
        }
    }

    private object ReducerImpl: Reducer<AddSavingsStore.State, Msg> {
        override fun AddSavingsStore.State.reduce(msg: Msg): AddSavingsStore.State =
            when (msg) {
                is Msg.AddSavingsLoaded -> copy(correlationId = msg.correlationId)
                is Msg.AddSavingsLoading -> copy(isLoading = msg.isLoading)
                is Msg.AddSavingsFailed -> copy(error = msg.error)
                is Msg.ClearError -> copy(error = msg.error)
            }
    }
}