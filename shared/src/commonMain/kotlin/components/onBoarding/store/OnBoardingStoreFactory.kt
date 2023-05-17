package components.onBoarding.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import network.onBoarding.data.OnBoardingRepository
import network.onBoarding.model.PrestaOnBoardingResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import prestaDispatchers

internal class OnBoardingStoreFactory(
    private val storeFactory: StoreFactory,
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
        data class OnBoardingLoaded(val onBoardingResponse: PrestaOnBoardingResponse) : Msg()
        data class OnBoardingFailed(val error: String?) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<OnBoardingStore.Intent, Unit, OnBoardingStore.State, Msg, Nothing>(
        prestaDispatchers.main) {
        override fun executeAction(action: Unit, getState: () -> OnBoardingStore.State) {

        }

        override fun executeIntent(intent: OnBoardingStore.Intent, getState: () -> OnBoardingStore.State): Unit =
            when (intent) {
                else -> {

                }
            }
    }

    private object ReducerImpl: Reducer<OnBoardingStore.State, Msg> {
        override fun OnBoardingStore.State.reduce(msg: Msg): OnBoardingStore.State =
            when (msg) {
                is Msg.OnBoardingLoading -> copy(isLoading = true)
                is Msg.OnBoardingLoaded -> OnBoardingStore.State(phoneNumber = phoneNumber)
                is Msg.OnBoardingFailed -> copy(error = msg.error)
            }
    }
}