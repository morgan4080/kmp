package com.presta.customer.ui.components.registration.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.registration.data.RegistrationRepository
import com.presta.customer.network.registration.model.PrestaRegistrationResponse
import com.presta.customer.ui.components.root.DefaultRootComponent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import prestaDispatchers

class RegistrationStoreFactory(
    private val storeFactory: StoreFactory,
    private val phoneNumber: String,
    private val isTermsAccepted: Boolean,
    private val isActive: Boolean,
    private val onBoardingContext: DefaultRootComponent.OnBoardingContext,
): KoinComponent {
    private val registrationRepository by inject<RegistrationRepository>()

    fun create(): RegistrationStore =
        object : RegistrationStore, Store<RegistrationStore.Intent, RegistrationStore.State, Nothing> by storeFactory.create(
            name = "RegistrationStore",
            initialState = RegistrationStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class RegistrationLoading(val isLoading: Boolean = true) : Msg()
        data class PrimeRegistration(val phoneNumber: String, val isTermsAccepted: Boolean, val isActive: Boolean, val onBoardingContext: DefaultRootComponent.OnBoardingContext) : Msg()
        data class RegistrationLoaded(val registrationResponse: PrestaRegistrationResponse) : Msg()
        data class RegistrationFailed(val error: String?) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<RegistrationStore.Intent, Unit, RegistrationStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {
        override fun executeAction(action: Unit, getState: () -> RegistrationStore.State) {
           dispatch(Msg.PrimeRegistration(
               phoneNumber,isTermsAccepted,isActive, onBoardingContext
           ))
        }

        override fun executeIntent(intent: RegistrationStore.Intent, getState: () -> RegistrationStore.State): Unit =
            when (intent) {
                is RegistrationStore.Intent.CreateMember ->
                    createMember(
                        token = intent.token,
                        firstName = intent.firstName,
                        lastName = intent.lastName,
                        phoneNumber = intent.phoneNumber,
                        idNumber = intent.idNumber,
                        tocsAccepted = intent.tocsAccepted,
                        tenantId = intent.tenantId
                    )
            }

        private var createMemberJob: Job? = null

        private fun createMember(
            token: String,
            firstName: String,
            lastName: String,
            phoneNumber: String,
            idNumber: String,
            tocsAccepted: Boolean,
            tenantId: String
        ) {
            if (createMemberJob?.isActive == true) return

            dispatch(Msg.RegistrationLoading())

            createMemberJob = scope.launch {
                registrationRepository.createMember(
                    token = token,
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    idNumber = idNumber,
                    tocsAccepted = tocsAccepted,
                    tenantId = tenantId
                ).onSuccess { response ->
                    dispatch(Msg.RegistrationLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.RegistrationFailed(e.message))
                }

                dispatch(Msg.RegistrationLoading(false))
            }
        }

    }

    private object ReducerImpl: Reducer<RegistrationStore.State, Msg> {
        override fun RegistrationStore.State.reduce(msg: Msg): RegistrationStore.State =
            when (msg) {
                is Msg.RegistrationLoading -> copy(isLoading = msg.isLoading)
                is Msg.PrimeRegistration -> copy(
                    phoneNumber = msg.phoneNumber,
                    isTermsAccepted = msg.isTermsAccepted,
                    isActive = msg.isTermsAccepted,
                    onBoardingContext = msg.onBoardingContext
                )
                is Msg.RegistrationFailed -> copy(error = msg.error)
                is Msg.RegistrationLoaded -> copy(registrationResponse = msg.registrationResponse)
            }
    }
}