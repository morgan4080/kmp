package com.presta.customer.ui.components.applyLongTermLoan.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.longTermLoans.data.LongTermLoansRepository
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse
import com.presta.customer.prestaDispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ApplyLongTermLoansStoreFactory(
    private val storeFactory: StoreFactory
) : KoinComponent {
    private val longTermLoansRepository by inject<LongTermLoansRepository>()

    fun create():ApplyLongTermLoansStore =
        object : ApplyLongTermLoansStore,
            Store<ApplyLongTermLoansStore.Intent, ApplyLongTermLoansStore.State, Nothing> by storeFactory.create(
                name = "LongTermLoansStore",
                initialState = ApplyLongTermLoansStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {
        data class LongTermLoansLoading (val isLoading: Boolean = true) : Msg()
        data class LongTermLoansLoaded(val longTermLoansLoaded:PrestaLongTermLoansProductResponse) :
            Msg()

        data class LongTermLoansFailed(val error: String?) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ApplyLongTermLoansStore.Intent, Unit, ApplyLongTermLoansStore.State, ApplyLongTermLoansStoreFactory.Msg, Nothing>(
            prestaDispatchers.main
        ) {
        override fun executeAction(action: Unit, getState: () -> ApplyLongTermLoansStore.State) {

        }

        override fun executeIntent(
            intent: ApplyLongTermLoansStore.Intent, getState: () -> ApplyLongTermLoansStore.State
        ): Unit =
            when (intent) {

                is ApplyLongTermLoansStore.Intent.GetLongTermLoansProducts -> getPrestalongTermLoanProducts(
                    token = intent.token

                )

            }

        private var getPrestaLongTermLoansProductsJob: Job? = null

        private fun getPrestalongTermLoanProducts(
            token: String
        ) {
            if (getPrestaLongTermLoansProductsJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getPrestaLongTermLoansProductsJob= scope.launch {
               longTermLoansRepository.getLonTermLoansData (
                    token = token
                ).onSuccess { response ->
                    dispatch(Msg.LongTermLoansLoaded(response))
                   println("Load Success")

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                   println("Load failed")
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

    }
    private object ReducerImpl :
        Reducer<ApplyLongTermLoansStore.State, Msg> {
        override fun ApplyLongTermLoansStore.State.reduce(msg: Msg): ApplyLongTermLoansStore.State =
            when (msg) {
                is Msg.LongTermLoansLoading -> copy(isLoading = msg.isLoading)
                is Msg.LongTermLoansFailed -> copy(error = msg.error)
                is Msg.LongTermLoansLoaded -> copy(prestaLongTermLoanProducts= msg.longTermLoansLoaded)

            }
    }

}