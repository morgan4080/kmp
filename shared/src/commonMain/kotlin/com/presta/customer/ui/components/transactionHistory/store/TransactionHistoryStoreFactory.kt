package com.presta.customer.ui.components.transactionHistory.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.profile.data.ProfileRepository
import com.presta.customer.network.profile.model.PrestaTransactionHistoryResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.presta.customer.prestaDispatchers

class TransactionHistoryStoreFactory(
    private val storeFactory: StoreFactory
): KoinComponent {
    private val profileRepository by inject<ProfileRepository>()
    
    fun create(): TransactionHistoryStore =
        object: TransactionHistoryStore, Store<TransactionHistoryStore.Intent, TransactionHistoryStore.State, Nothing> by storeFactory.create(
            name = "TransactionHistoryStore",
            initialState = TransactionHistoryStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}


    private sealed class Msg {
        data class TransactionHistoryLoading(val isLoading: Boolean = true): Msg()
        data class TransactionHistoryLoaded(val transactionHistory: List<PrestaTransactionHistoryResponse>): Msg()
        data class TransactionMappingLoaded(val transactionMapping: Map<String, String>): Msg()
        data class TransactionHistoryFailed(val error: String?): Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<TransactionHistoryStore.Intent, Unit, TransactionHistoryStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {
        override fun executeAction(action: Unit, getState: () -> TransactionHistoryStore.State) {

        }

        override fun executeIntent(intent: TransactionHistoryStore.Intent, getState: () -> TransactionHistoryStore.State): Unit =
            when(intent) {
                is TransactionHistoryStore.Intent.GetTransactionHistory -> getTransactionHistory(token = intent.token, refId = intent.refId, purposeIds = intent.purposeIds, searchTerm = intent.searchTerm)
                is TransactionHistoryStore.Intent.GetTransactionMapping -> getTransactionMapping(token = intent.token)
            }


        private var getTransactionHistoryJob: Job? = null

        private fun getTransactionHistory(
            token: String,
            refId: String,
            purposeIds: List<String>,
            searchTerm: String?
        ) {
            if (getTransactionHistoryJob?.isActive == true) return

            dispatch(Msg.TransactionHistoryLoading())

            getTransactionHistoryJob = scope.launch {
                profileRepository.getTransactionHistoryData(
                    token = token,
                    memberRefId = refId,
                    purposeIds = purposeIds,
                    searchTerm = searchTerm
                ).onSuccess { response ->
                    dispatch(Msg.TransactionHistoryLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.TransactionHistoryFailed(e.message))
                }
            }
        }
        //Get Transaction mapping

        private var getTransactionMappingJob: Job? = null

        private fun getTransactionMapping(
            token: String
        ) {
            if (getTransactionMappingJob?.isActive == true) return

            dispatch(Msg.TransactionHistoryLoading())

            getTransactionMappingJob = scope.launch {
                profileRepository.getTransactionMappingData(
                    token = token
                ).onSuccess { response ->
                    dispatch(Msg.TransactionMappingLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.TransactionHistoryFailed(e.message))
                }
            }
        }
    }

    private object ReducerImpl: Reducer<TransactionHistoryStore.State, Msg> {
        override fun TransactionHistoryStore.State.reduce(msg: Msg): TransactionHistoryStore.State =
            when (msg) {
                is Msg.TransactionHistoryLoading -> copy(isLoading = msg.isLoading)
                is Msg.TransactionHistoryFailed -> copy(error = msg.error)
                is Msg.TransactionHistoryLoaded -> copy(transactionHistory = msg.transactionHistory)
                is Msg.TransactionMappingLoaded -> copy(transactionMapping = msg.transactionMapping)
            }
    }
}