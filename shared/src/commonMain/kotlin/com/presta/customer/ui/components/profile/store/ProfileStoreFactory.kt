package com.presta.customer.ui.components.profile.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.profile.data.ProfileRepository
import com.presta.customer.network.profile.model.PrestaLoansBalancesResponse
import com.presta.customer.network.profile.model.PrestaSavingsBalancesResponse
import com.presta.customer.network.profile.model.PrestaTransactionHistoryResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.presta.customer.prestaDispatchers

class ProfileStoreFactory(
    private val storeFactory: StoreFactory
): KoinComponent {
    private val profileRepository by inject<ProfileRepository>()

    fun create(): ProfileStore =
        object: ProfileStore, Store<ProfileStore.Intent, ProfileStore.State, Nothing> by storeFactory.create(
            name = "ProfileStore",
            initialState = ProfileStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ProfileLoading(val isLoading: Boolean = true): Msg()
        data class ProfileLoansLoaded(val loanBalances: PrestaLoansBalancesResponse): Msg()
        data class ProfileSavingsLoaded(val savingBalances: PrestaSavingsBalancesResponse): Msg()
        data class TransactionHistoryLoaded(val transactionHistory: List<PrestaTransactionHistoryResponse>): Msg()
        data class TransactionMappingLoaded(val transactionMapping: Map<String, String>): Msg()
        data class ProfileFailed(val error: String?): Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<ProfileStore.Intent, Unit, ProfileStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {
        override fun executeAction(action: Unit, getState: () -> ProfileStore.State) {

        }

        override fun executeIntent(intent: ProfileStore.Intent, getState: () -> ProfileStore.State): Unit =
            when(intent) {
                is ProfileStore.Intent.GetSavingsBalances -> getSavingsBalances(token = intent.token, refId = intent.refId)
                is ProfileStore.Intent.GetLoanBalances -> getLoanBalances(token = intent.token, refId = intent.refId)
                is ProfileStore.Intent.GetTransactionHistory -> getTransactionHistory(token = intent.token, refId = intent.refId, purposeIds = intent.purposeIds)
                is ProfileStore.Intent.GetTransactionMapping -> getTransactionMapping(token = intent.token)
            }

        private var getSavingsBalancesJob: Job? = null

        private fun getSavingsBalances(
            token: String,
            refId: String
        ) {
            if (getSavingsBalancesJob?.isActive == true) return

            dispatch(Msg.ProfileLoading())

            getSavingsBalancesJob = scope.launch {
                profileRepository.getUserSavingsData(
                    token = token,
                    memberRefId = refId
                ).onSuccess { response ->
                    dispatch(Msg.ProfileSavingsLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.ProfileFailed(e.message))
                }
            }
        }

        private var getLoanBalancesJob: Job? = null

        private fun getLoanBalances(
            token: String,
            refId: String
        ) {
            if (getLoanBalancesJob?.isActive == true) return

            dispatch(Msg.ProfileLoading())

            getLoanBalancesJob = scope.launch {
                profileRepository.getUserLoansData(
                    token = token,
                    memberRefId = refId
                ).onSuccess { response ->
                    dispatch(Msg.ProfileLoansLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.ProfileFailed(e.message))
                }
                dispatch(Msg.ProfileLoading(false))
            }
        }

        private var getTransactionHistoryJob: Job? = null

        private fun getTransactionHistory(
            token: String,
            refId: String,
            purposeIds: List<String>,
        ) {
            if (getTransactionHistoryJob?.isActive == true) return

            dispatch(Msg.ProfileLoading())

            getTransactionHistoryJob = scope.launch {
                profileRepository.getTransactionHistoryData(
                    token = token,
                    memberRefId = refId,
                    purposeIds = purposeIds,
                    searchTerm = null
                ).onSuccess { response ->
                    dispatch(Msg.TransactionHistoryLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.ProfileFailed(e.message))
                }

                dispatch(Msg.ProfileLoading(false))
            }
        }
        //Get Transaction mapping

        private var getTransactionMappingJob: Job? = null

        private fun getTransactionMapping(
            token: String
        ) {
            if (getTransactionMappingJob?.isActive == true) return

            dispatch(Msg.ProfileLoading())

            getTransactionMappingJob = scope.launch {
                profileRepository.getTransactionMappingData(
                    token = token
                ).onSuccess { response ->
                    dispatch(Msg.TransactionMappingLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.ProfileFailed(e.message))
                }

                dispatch(Msg.ProfileLoading(false))
            }
        }


    }

    private object ReducerImpl: Reducer<ProfileStore.State, Msg> {
        override fun ProfileStore.State.reduce(msg: Msg): ProfileStore.State =
            when (msg) {
                is Msg.ProfileLoading -> copy(isLoading = msg.isLoading)
                is Msg.ProfileLoansLoaded -> copy(loansBalances = msg.loanBalances)
                is Msg.ProfileSavingsLoaded -> copy(savingsBalances = msg.savingBalances)
                is Msg.ProfileFailed -> copy(error = msg.error)
                is Msg.TransactionHistoryLoaded->copy(transactionHistory = msg.transactionHistory)
                is Msg.TransactionMappingLoaded->copy(transactionMapping = msg.transactionMapping)
            }
    }
}