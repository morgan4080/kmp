package com.presta.customer.ui.components.shortTermLoans.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.shortTermLoans.data.ShortTermLoansRepository
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermProductsListResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import prestaDispatchers

class ShortTermLoansStoreFactory(
    private val storeFactory: StoreFactory
) : KoinComponent {
    private val shortTermLoansRepository by inject<ShortTermLoansRepository>()

    fun create(): ShortTermLoansStore =
        object : ShortTermLoansStore,
            Store<ShortTermLoansStore.Intent, ShortTermLoansStore.State, Nothing> by storeFactory.create(
                name = "ShortTermLoansStore",
                initialState = ShortTermLoansStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {
        data class ShortTermLoansLoading(val isLoading: Boolean = true) : Msg()
        data class ShortTermLoansProductsListLoaded(val shortTermLoansProductList: PrestaShortTermProductsListResponse) :
            Msg()

        data class ShortTermLoansFailed(val error: String?) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ShortTermLoansStore.Intent, Unit, ShortTermLoansStore.State, Msg, Nothing>(
            prestaDispatchers.main
        ) {
        override fun executeAction(action: Unit, getState: () -> ShortTermLoansStore.State) {

        }

        override fun executeIntent(
            intent: ShortTermLoansStore.Intent, getState: () -> ShortTermLoansStore.State
        ): Unit =
            when (intent) {
                is ShortTermLoansStore.Intent.GetPrestaShortTermProductList -> getTransactionHistory(
                    token = intent.token,
                    refId = intent.refId
                )
            }
        //Get ShortTermLoansProducts
        private var getShortTermProductListJob: Job? = null

        private fun getTransactionHistory(
            token: String,
            refId: String
        ) {
            if (getShortTermProductListJob?.isActive == true) return

            dispatch(Msg.ShortTermLoansLoading())

            getShortTermProductListJob = scope.launch {
                shortTermLoansRepository.getShortTermProductListData(
                    token = token,
                    memberRefId = refId
                ).onSuccess { response ->
                    dispatch(Msg.ShortTermLoansProductsListLoaded(response))


                }.onFailure { e ->
                    dispatch(Msg.ShortTermLoansFailed(e.message))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<ShortTermLoansStore.State, Msg> {
        override fun ShortTermLoansStore.State.reduce(msg: Msg): ShortTermLoansStore.State =
            when (msg) {
                is Msg.ShortTermLoansLoading -> copy(isLoading = msg.isLoading)
                is Msg.ShortTermLoansFailed -> copy(error = msg.error)
                is Msg.ShortTermLoansProductsListLoaded -> copy(prestaShortTermProductList = msg.shortTermLoansProductList)
            }
    }
}