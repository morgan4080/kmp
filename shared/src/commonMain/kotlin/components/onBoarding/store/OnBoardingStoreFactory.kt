package components.onBoarding.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import components.auth.store.AuthStoreFactory
import components.root.DefaultRootComponent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import network.onBoarding.data.OnBoardingRepository
import network.onBoarding.model.PrestaOnBoardingResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import prestaDispatchers

internal class OnBoardingStoreFactory(
    private val storeFactory: StoreFactory,
    private val country: String,
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
        object OnBoardingLoading : Msg()
        data class OnBoardingGetMemberLoaded(val onBoardingResponse: PrestaOnBoardingResponse) : Msg()
        data class OnBoardingDefaultCountry(val country: String) : Msg()
        data class OnBoardingContext(val onBoardingContext: DefaultRootComponent.OnBoardingContext) : Msg()
        data class OnBoardingFailed(val error: String?) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<OnBoardingStore.Intent, Unit, OnBoardingStore.State, Msg, Nothing>(
        prestaDispatchers.main) {
        // runs when component is initialized
        override fun executeAction(action: Unit, getState: () -> OnBoardingStore.State) {
            dispatch(Msg.OnBoardingDefaultCountry(country))
            dispatch(Msg.OnBoardingContext(onBoardingContext))
        }

        override fun executeIntent(intent: OnBoardingStore.Intent, getState: () -> OnBoardingStore.State): Unit =
            when (intent) {
                is OnBoardingStore.Intent.GetMemberDetails ->
                    getMemberDetails(token = intent.token, memberIdentifier = intent.memberIdentifier, identifierType = intent.identifierType)
                is OnBoardingStore.Intent.UpdateMember ->
                    updateMemberDetails(token = intent.token,memberRefId = intent.memberRefId, intent.pinConfirmation)
            }

        private var getMemberJob: Job? = null

        private fun getMemberDetails(
            token: String,
            memberIdentifier: String,
            identifierType: IdentifierTypes
        ) {
            if (getMemberJob?.isActive == true) return

            getMemberJob = scope.launch {
                dispatch(Msg.OnBoardingLoading)
                onBoardingRepository.getOnBoardingMemberData(
                    token,
                    memberIdentifier,
                    identifierType
                ).onSuccess {response ->
                    dispatch(Msg.OnBoardingGetMemberLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.OnBoardingFailed(e.message))
                }
            }
        }

        private var updateMemberJob: Job? = null

        private fun updateMemberDetails(
            token: String,
            memberRefId: String,
            pinConfirmation: String
        ) {
            if (updateMemberJob?.isActive == true) return

            updateMemberJob = scope.launch {
                dispatch(Msg.OnBoardingLoading)
                onBoardingRepository.updateMemberData(
                    token,
                    memberRefId,
                    pinConfirmation
                ).onSuccess {response ->
                    println("::::::::member update success")
                    println(response)
                   // dispatch action to
                }.onFailure { e ->
                    println("::::::::member update failure")
                    println(e.message)
                    dispatch(Msg.OnBoardingFailed(e.message))
                }
            }
        }
    }

    private object ReducerImpl: Reducer<OnBoardingStore.State, Msg> {
        override fun OnBoardingStore.State.reduce(msg: Msg): OnBoardingStore.State =
            when (msg) {
                is Msg.OnBoardingLoading -> copy(isLoading = true)
                is Msg.OnBoardingGetMemberLoaded -> copy(phoneNumber = msg.onBoardingResponse.phoneNumber)
                is Msg.OnBoardingDefaultCountry -> copy(country = msg.country)
                is Msg.OnBoardingContext -> copy(onBoardingContext = msg.onBoardingContext)
                is Msg.OnBoardingFailed -> copy(error = msg.error)
            }
    }
}