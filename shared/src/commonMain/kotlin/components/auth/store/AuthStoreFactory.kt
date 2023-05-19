package components.auth.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import network.authDevice.data.AuthRepository
import network.authDevice.model.PrestaAuthResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import prestaDispatchers

internal class AuthStoreFactory(
    private val storeFactory: StoreFactory,
): KoinComponent {
    private val authRepository by inject<AuthRepository>()

    fun create(): AuthStore =
        object : AuthStore, Store<AuthStore.Intent, AuthStore.State, Nothing> by storeFactory.create(
            name = "AuthStore",
            initialState = AuthStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        object AuthLoading : Msg()
        data class AuthLoaded(val authResponse: PrestaAuthResponse) : Msg()
        data class AuthFailed(val error: String?) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<AuthStore.Intent, Unit, AuthStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {
        override fun executeAction(action: Unit, getState: () -> AuthStore.State) {

        }

        override fun executeIntent(intent: AuthStore.Intent, getState: () -> AuthStore.State): Unit =
            when (intent) {
                is AuthStore.Intent.AuthenticateClient -> authClient(intent.client_secret)
                is AuthStore.Intent.LoginUser -> loginUser(intent.phoneNumber, intent.pin, intent.tenant, intent.clientSecret)
            }

        private var authClientJob: Job? = null

        private fun authClient(
            client_secret: String
        ) {
            if (authClientJob?.isActive == true) return

            authClientJob  = scope.launch {
                dispatch(Msg.AuthLoading)
                authRepository
                    .postClientAuthDetails(client_secret)
                    .onSuccess {response ->
                        if (response.access_token.isEmpty()) {
                            dispatch(Msg.AuthFailed("Access Token Empty"))
                        } else {
                            dispatch(Msg.AuthLoaded(response))
                        }

                    }
                    .onFailure { e ->
                        dispatch(Msg.AuthFailed(e.message))
                    }
            }
        }

        private var loginUserJob: Job? = null

        private fun loginUser(
            phoneNumber: String,
            pin: String,
            tenant: String,
            clientSecret: String
        ) {
            if (loginUserJob?.isActive == true) return

            loginUserJob  = scope.launch {
                dispatch(Msg.AuthLoading)
                // loginUserJob
                /*authRepository
                    .postClientAuthDetails(client_secret)
                    .onSuccess {response ->
                        if (response.access_token.isEmpty()) {
                            dispatch(Msg.AuthFailed("Access Token Empty"))
                        } else {
                            dispatch(Msg.AuthLoaded(response))
                        }

                    }
                    .onFailure { e ->
                        dispatch(Msg.AuthFailed(e.message))
                    }*/
            }
        }
    }

    private object ReducerImpl: Reducer<AuthStore.State, Msg> {
        override fun AuthStore.State.reduce(msg: Msg): AuthStore.State =
            when (msg) {
                is Msg.AuthLoading -> copy(isLoading = true)
                is Msg.AuthLoaded -> {
                    AuthStore.State(access_token = access_token)
                }
                is Msg.AuthFailed -> copy(error = msg.error)
            }
    }
}