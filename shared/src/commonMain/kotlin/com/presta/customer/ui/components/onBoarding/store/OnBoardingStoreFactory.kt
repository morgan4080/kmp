package com.presta.customer.ui.components.onBoarding.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.presta.customer.network.onBoarding.data.OnBoardingRepository
import com.presta.customer.network.onBoarding.model.PrestaOnBoardingResponse
import com.presta.customer.network.onBoarding.model.PrestaUpdateMemberResponse
import com.presta.customer.ui.components.root.DefaultRootComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.presta.customer.prestaDispatchers

internal class OnBoardingStoreFactory(
    private val storeFactory: StoreFactory,
    private val onBoardingContext: DefaultRootComponent.OnBoardingContext,
): KoinComponent {
    private val onBoardingRepository by inject<OnBoardingRepository>()

    fun create(): OnBoardingStore =
        object : OnBoardingStore, Store<OnBoardingStore.Intent, OnBoardingStore.State, Nothing> by storeFactory.create(
            name = "OnBoardingStore",
            initialState = OnBoardingStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class OnBoardingLoading(val isLoading: Boolean = true) : Msg()
        data class OnBoardingGetMemberLoaded(val onBoardingResponse: PrestaOnBoardingResponse) : Msg()
        data class OnBoardingUpdateMemberLoaded(val updateMemberResponse: PrestaUpdateMemberResponse) : Msg()
        data class OnBoardingContext(val onBoardingContext: DefaultRootComponent.OnBoardingContext) : Msg()
        data class OnSelectedCountry(val countrySelected: Country) : Msg()
        data class OnBoardingFailed(val error: String?) : Msg()
        data class OnBoardingClearMember(val member: PrestaOnBoardingResponse?) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<OnBoardingStore.Intent, Unit, OnBoardingStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {
        // runs when component is initialized
        override fun executeAction(action: Unit, getState: () -> OnBoardingStore.State) {
            dispatch(Msg.OnBoardingContext(onBoardingContext))
        }

        override fun executeIntent(intent: OnBoardingStore.Intent, getState: () -> OnBoardingStore.State): Unit =
            when (intent) {
                is OnBoardingStore.Intent.GetMemberDetails ->
                    getMemberDetails(token = intent.token, memberIdentifier = intent.memberIdentifier, identifierType = intent.identifierType, tenantId = intent.tenantId)
                is OnBoardingStore.Intent.UpdateMember ->
                    updateMemberDetails(token = intent.token, memberRefId = intent.memberRefId, intent.pinConfirmation, tenantId = intent.tenantId)
                is OnBoardingStore.Intent.SelectCountry ->
                    updateSelectedCountry(countrySelected = intent.country)
                is OnBoardingStore.Intent.UpdateError -> updateError(error = intent.error)
                is OnBoardingStore.Intent.ClearMember ->
                    clearMember(member = intent.member)
            }

        private var getMemberJob: Job? = null

        private fun getMemberDetails(
            token: String,
            memberIdentifier: String,
            identifierType: IdentifierTypes,
            tenantId: String
        ) {
            if (getMemberJob?.isActive == true) return

            dispatch(Msg.OnBoardingLoading())

            getMemberJob = scope.launch {
                onBoardingRepository.getOnBoardingMemberData(
                    token = token,
                    memberIdentifier = memberIdentifier,
                    identifierType = identifierType,
                    tenantId = tenantId
                ).onSuccess {response ->
                    dispatch(Msg.OnBoardingGetMemberLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.OnBoardingFailed(e.message))
                }

                dispatch(Msg.OnBoardingLoading(false))
            }
        }

        private var updateMemberJob: Job? = null

        private fun updateMemberDetails(
            token: String,
            memberRefId: String,
            pinConfirmation: String,
            tenantId: String
        ) {
            if (updateMemberJob?.isActive == true) return

            dispatch(Msg.OnBoardingLoading())

            updateMemberJob = scope.launch {
                onBoardingRepository.updateOnBoardingMemberPinAndTerms(
                    token = token,
                    memberRefId = memberRefId,
                    pinConfirmation = pinConfirmation,
                    tenantId = tenantId
                ).onSuccess { response ->
                    dispatch(Msg.OnBoardingUpdateMemberLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.OnBoardingFailed(e.message))
                }

                dispatch(Msg.OnBoardingLoading(false))
            }
        }

        private fun updateSelectedCountry(countrySelected: Country) {
            dispatch(Msg.OnSelectedCountry(countrySelected))
        }

        private fun updateError(error: String?) {
            dispatch(Msg.OnBoardingFailed(error))
        }

        private fun clearMember(member: PrestaOnBoardingResponse?) {
            dispatch(Msg.OnBoardingClearMember(member))
        }
    }

    private object ReducerImpl: Reducer<OnBoardingStore.State, Msg> {
        override fun OnBoardingStore.State.reduce(msg: Msg): OnBoardingStore.State =
            when (msg) {
                is Msg.OnBoardingLoading -> copy(isLoading = msg.isLoading)
                is Msg.OnBoardingGetMemberLoaded -> copy(member = msg.onBoardingResponse)
                is Msg.OnBoardingUpdateMemberLoaded -> copy(updateMemberResponse = msg.updateMemberResponse)
                is Msg.OnBoardingContext -> copy(onBoardingContext = msg.onBoardingContext)
                is Msg.OnSelectedCountry -> copy(country = msg.countrySelected)
                is Msg.OnBoardingClearMember -> copy(member = msg.member)
                is Msg.OnBoardingFailed -> copy(error = msg.error)
            }
    }
}