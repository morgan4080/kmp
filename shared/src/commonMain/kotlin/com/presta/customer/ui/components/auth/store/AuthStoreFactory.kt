package com.presta.customer.ui.components.auth.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.authDevice.data.AuthRepository
import com.presta.customer.network.authDevice.model.PrestaCheckAuthUserResponse
import com.presta.customer.network.authDevice.model.PrestaLogInResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import prestaDispatchers

internal class AuthStoreFactory(
    private val storeFactory: StoreFactory,
    private val phoneNumber: String?,
    private val isTermsAccepted: Boolean,
    private val isActive: Boolean,
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
        data class AuthLoading(val isLoading: Boolean = true) : Msg()
        data class AuthInitData(val phoneNumber: String?, val isTermsAccepted: Boolean, val isActive: Boolean) : Msg()
        data class UpdateContext(
            val context: Contexts,
            val title: String,
            val label: String,
            val pinCreated: Boolean,
            val pinConfirmed: Boolean,
            val error: String?
        ) : Msg()
        data class LoginFulfilled(val logInResponse: PrestaLogInResponse) : Msg()

        data class CheckAuthenticatedUserLoaded(val authUserResponse: PrestaCheckAuthUserResponse): Msg()
        data class AuthFailed(val error: String?) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<AuthStore.Intent, Unit, AuthStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {
        override fun executeAction(action: Unit, getState: () -> AuthStore.State) {
            dispatch(Msg.AuthInitData(phoneNumber, isTermsAccepted, isActive))
        }

        override fun executeIntent(intent: AuthStore.Intent, getState: () -> AuthStore.State): Unit =
            when (intent) {
                is AuthStore.Intent.LoginUser -> loginUser(intent.phoneNumber, intent.pin, intent.tenantId)
                is AuthStore.Intent.CheckAuthenticatedUser -> checkAuthenticatedUser(intent.token)
                is AuthStore.Intent.UpdateError -> updateError(error = intent.error)
                is AuthStore.Intent.GetCachedToken -> getUserAuthToken()
                is AuthStore.Intent.UpdateContext -> dispatch(Msg.UpdateContext(
                    context = intent.context,
                    title = intent.title,
                    label = intent.label,
                    pinCreated = intent.pinCreated,
                    pinConfirmed = intent.pinConfirmed,
                    error = intent.error,
                ))
            }

        private var loginUserJob: Job? = null

        private fun loginUser(
            phoneNumber: String,
            pin: String,
            tenantId: String
        ) {
            if (loginUserJob?.isActive == true) return

            dispatch(Msg.AuthLoading())

            loginUserJob  = scope.launch {
                authRepository
                    .loginUser(phoneNumber, pin, tenantId)
                    .onSuccess {response ->
                        dispatch(Msg.LoginFulfilled(response))
                    }
                    .onFailure { e ->
                        dispatch(Msg.AuthFailed(e.message))
                    }

                dispatch(Msg.AuthLoading(false))
            }
        }

        private var checkPinJob: Job? = null

        private fun updateError(error: String?) {
            dispatch(Msg.AuthFailed(error))
        }

        private var checkAuthenticatedUserJob: Job? = null

        private fun checkAuthenticatedUser(token: String) {
            if (checkAuthenticatedUserJob?.isActive == true) return

            dispatch(Msg.AuthLoading())

            checkAuthenticatedUserJob = scope.launch {
                authRepository.checkAuthenticatedUser(token)
                    .onSuccess { response ->
                        dispatch(Msg.CheckAuthenticatedUserLoaded(response))
                    }
                    .onFailure { e ->
                        dispatch(Msg.AuthFailed(e.message))
                    }

                dispatch(Msg.AuthLoading(false))
            }
        }

        private var getUserAuthTokenJob: Job? = null

        private fun getUserAuthToken() {
            if (getUserAuthTokenJob?.isActive == true) return

            getUserAuthTokenJob = scope.launch {
                authRepository.getUserAuthToken()
                    .onSuccess { response ->
                        dispatch(Msg.LoginFulfilled(response))
                    }
                    .onFailure { e ->
                        dispatch(Msg.AuthFailed(e.message))
                    }
            }
        }
    }

    private object ReducerImpl: Reducer<AuthStore.State, Msg> {
        override fun AuthStore.State.reduce(msg: Msg): AuthStore.State =
            when (msg) {
                is Msg.AuthLoading -> copy(isLoading = msg.isLoading)
                is Msg.CheckAuthenticatedUserLoaded -> copy(authUserResponse = msg.authUserResponse)
                is Msg.AuthInitData -> copy(
                    phoneNumber = msg.phoneNumber,
                    isTermsAccepted = msg.isTermsAccepted,
                    isActive = msg.isActive
                )
                is Msg.UpdateContext -> copy(
                    context = msg.context,
                    title = msg.title,
                    label = msg.label,
                    pinCreated = msg.pinCreated,
                    pinConfirmed = msg.pinConfirmed,
                    error = msg.error
                )
                is Msg.LoginFulfilled -> copy(loginResponse = msg.logInResponse)
                is Msg.AuthFailed -> copy(error = msg.error)
            }
    }
}