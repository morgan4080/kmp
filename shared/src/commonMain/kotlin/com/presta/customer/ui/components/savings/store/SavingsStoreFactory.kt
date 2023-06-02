package com.presta.customer.ui.components.savings.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.profile.data.ProfileRepository
import com.presta.customer.network.profile.model.PrestaSavingsBalancesResponse
import com.presta.customer.network.profile.model.PrestaTransactionHistoryResponse
import com.presta.customer.ui.components.profile.store.ProfileStoreFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import prestaDispatchers

class SavingsStoreFactory (
    private val storeFactory: StoreFactory
): KoinComponent {
    private val profileRepository by inject<ProfileRepository>()

    fun create(): SavingsStore =
        object: SavingsStore, Store<SavingsStore.Intent, SavingsStore.State, Nothing> by storeFactory.create(
            name = "SavingsStore",
            initialState = SavingsStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}


    private sealed class Msg {
        data class SavingsTransactionsLoading(val isLoading: Boolean = true): Msg()
        data class SavingsTransactionsLoaded(val savingsTransactions: List<PrestaTransactionHistoryResponse>): Msg()
        data class TransactionsMappingLoaded(val transactionMapping: Map<String, String>): Msg()
        data class SavingsLoaded(val savingBalances: PrestaSavingsBalancesResponse): Msg()
        data class SavingsTransactionsFailed(val error: String?): Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<SavingsStore.Intent, Unit, SavingsStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {
        override fun executeAction(action: Unit, getState: () -> SavingsStore.State) {

        }

        override fun executeIntent(intent: SavingsStore.Intent, getState: () -> SavingsStore.State): Unit =
            when(intent) {
                is SavingsStore.Intent.GetSavingsTransactions -> getSavingsTransactions(token = intent.token, refId = intent.refId, purposeIds = intent.purposeIds, searchTerm = intent.searchTerm)
                is SavingsStore.Intent.GetTransactionsMapping -> getTransactionMapping(token = intent.token)
                is SavingsStore.Intent.GetSavingsBalances -> getSavingsBalances(token = intent.token, refId = intent.refId)
            }


        private var getSavingsBalancesJob: Job? = null

        private fun getSavingsBalances(
            token: String,
            refId: String
        ) {
            if (getSavingsBalancesJob?.isActive == true) return

            dispatch(Msg.SavingsTransactionsLoading())

            getSavingsBalancesJob = scope.launch {
                profileRepository.getUserSavingsData(
                    token = token,
                    memberRefId = refId
                ).onSuccess { response ->
                    dispatch(Msg.SavingsLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.SavingsTransactionsFailed(e.message))
                }

                Msg.SavingsTransactionsLoading(false)
            }
        }

        private var getSavingsTransactionsJob: Job? = null

        private fun getSavingsTransactions(
            token: String,
            refId: String,
            purposeIds: List<String>,
            searchTerm: String?
        ) {
            if (getSavingsTransactionsJob?.isActive == true) return

            dispatch(Msg.SavingsTransactionsLoading())

            getSavingsTransactionsJob = scope.launch {
                profileRepository.getTransactionHistoryData(
                    token = token,
                    memberRefId = refId,
                    purposeIds = purposeIds,
                    searchTerm = searchTerm
                ).onSuccess { response ->
                    dispatch(Msg.SavingsTransactionsLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.SavingsTransactionsFailed(e.message))
                }
            }
        }
        //Get Transaction mapping

        private var getTransactionMappingJob: Job? = null

        private fun getTransactionMapping(
            token: String
        ) {
            if (getTransactionMappingJob?.isActive == true) return

            dispatch(Msg.SavingsTransactionsLoading())

            getTransactionMappingJob = scope.launch {
                profileRepository.getTransactionMappingData(
                    token = token
                ).onSuccess { response ->
                    dispatch(Msg.TransactionsMappingLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.SavingsTransactionsFailed(e.message))
                }
            }
        }
    }

    private object ReducerImpl: Reducer<SavingsStore.State, Msg> {
        override fun SavingsStore.State.reduce(msg: Msg): SavingsStore.State =
            when (msg) {
                is Msg.SavingsTransactionsLoading -> copy(isLoading = msg.isLoading)
                is Msg.SavingsTransactionsFailed -> copy(error = msg.error)
                is Msg.SavingsTransactionsLoaded -> copy(savingsTransactions = msg.savingsTransactions)
                is Msg.TransactionsMappingLoaded -> copy(transactionMapping = msg.transactionMapping)
                is Msg.SavingsLoaded -> copy(savingsBalances = msg.savingBalances)
            }
    }
}