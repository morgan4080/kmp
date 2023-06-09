package com.presta.customer.ui.components.registration.store

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.network.registration.data.RegistrationRepository
import com.presta.customer.network.registration.model.PrestaRegistrationResponse
import com.presta.customer.ui.components.root.DefaultRootComponent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.presta.customer.prestaDispatchers

class RegistrationStoreFactory(
    private val storeFactory: StoreFactory,
    private val phoneNumber: String,
    private val isTermsAccepted: Boolean,
    private val isActive: Boolean,
    private val pinStatus: PinStatus?,
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
        data class PrimeRegistration(val phoneNumber: String, val isTermsAccepted: Boolean, val isActive: Boolean, val onBoardingContext: DefaultRootComponent.OnBoardingContext, val pinStatus: PinStatus?) : Msg()
        data class RegistrationLoaded(val registrationResponse: PrestaRegistrationResponse) : Msg()
        data class RegistrationFailed(val error: String?) : Msg()
        object ClearError: Msg()
        data class UpdateInputValue(val inputField: InputFields, val value: TextFieldValue) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<RegistrationStore.Intent, Unit, RegistrationStore.State, Msg, Nothing>(
        prestaDispatchers.main
    ) {
        override fun executeAction(action: Unit, getState: () -> RegistrationStore.State) {
           dispatch(Msg.PrimeRegistration(
               phoneNumber,isTermsAccepted,isActive, onBoardingContext, pinStatus
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
                is RegistrationStore.Intent.UpdateInputValue ->
                    dispatch(
                        Msg.UpdateInputValue(
                            inputField = intent.inputField,
                            value = intent.value
                        )
                    )
                is RegistrationStore.Intent.ClearError ->
                    dispatch(Msg.ClearError)
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
                    onBoardingContext = msg.onBoardingContext,
                    pinStatus = msg.pinStatus
                )
                is Msg.RegistrationFailed -> copy(error = msg.error)
                is Msg.UpdateInputValue -> {
                    when(msg.inputField) {
                        InputFields.FIRST_NAME -> {
                            // validate first name
                            val pattern = Regex("^([a-zA-Z]+)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && firstName.required) {
                                errorMsg = "First name is required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Not a valid first name."
                                }
                            }
                            copy(
                                firstName = firstName.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }

                        InputFields.LAST_NAME -> {
                            // validate last name
                            val pattern = Regex("^([a-zA-Z]+)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && lastName.required) {
                                errorMsg = "Last name is required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Not a valid last name."
                                }
                            }
                            copy(
                                lastName = lastName.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }

                        InputFields.EMAIL -> {
                            // validate email
                            val pattern = Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~\\-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~\\-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && email.required) {
                                errorMsg = "Email is required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Not a valid email address"
                                }
                            }
                            copy(
                                email = email.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }

                        InputFields.ID_NUMBER -> {
                            // validate id number
                            val pattern = Regex("^([0-9]+)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && idNumber.required) {
                                errorMsg = "ID number is required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Not a valid id number"
                                }
                            }
                            copy(
                                idNumber = idNumber.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }

                        InputFields.INTRODUCER -> {
                            // validate introducer
                            val pattern = Regex("^([0-9]+)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && introducer.required) {
                                errorMsg = "Introducer ID number is required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Not a valid introducer id number"
                                }
                            }
                            copy(
                                introducer = introducer.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }
                    }
                }
                is Msg.ClearError -> copy(
                    error = null
                )
                is Msg.RegistrationLoaded -> copy(registrationResponse = msg.registrationResponse)
            }
    }
}