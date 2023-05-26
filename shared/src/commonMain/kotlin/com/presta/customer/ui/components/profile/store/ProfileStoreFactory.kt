package com.presta.customer.ui.components.profile.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.profile.data.ProfileRepository
import com.presta.customer.network.profile.model.PrestaBalancesResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import prestaDispatchers

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
        data class ProfileLoaded(val balances: PrestaBalancesResponse): Msg()
        data class ProfileFailed(val error: String?): Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<ProfileStore.Intent, Unit, ProfileStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {
        override fun executeAction(action: Unit, getState: () -> ProfileStore.State) {

        }

        override fun executeIntent(intent: ProfileStore.Intent, getState: () -> ProfileStore.State): Unit =
            when(intent) {
                is ProfileStore.Intent.GetBalances -> getBalances(token = intent.token, refId = intent.refId)
            }

        private var getBalancesJob: Job? = null

        private fun getBalances(
            token: String,
            refId: String
        ) {
            if (getBalancesJob?.isActive == true) return

            dispatch(Msg.ProfileLoading())

            getBalancesJob = scope.launch {
                profileRepository.getBalancesData(
                    token = token,
                    memberRefId = refId
                ).onSuccess { response ->
                    dispatch(Msg.ProfileLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.ProfileFailed(e.message))
                }
            }
        }

    }

    private object ReducerImpl: Reducer<ProfileStore.State, Msg> {
        override fun ProfileStore.State.reduce(msg: Msg): ProfileStore.State =
            when (msg) {
                is Msg.ProfileLoading -> copy(isLoading = msg.isLoading)
                is Msg.ProfileLoaded -> copy(balances = msg.balances)
                is Msg.ProfileFailed -> copy(error = msg.error)
            }
    }
}