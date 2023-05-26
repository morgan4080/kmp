package com.presta.customer.ui.components.registration.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.registration.model.PrestaRegistrationResponse
import com.presta.customer.ui.components.root.DefaultRootComponent
import com.presta.customer.ui.composables.InputTypes


enum class InputFields {
    FIRST_NAME,
    LAST_NAME,
    EMAIL,
    ID_NUMBER,
    GENDER,
    INTRODUCER
}

data class InputMethod(val inputLabel: String, val fieldType: InputFields, val required: Boolean, val inputTypes: InputTypes, val errorMessage: String)

interface RegistrationStore: Store<RegistrationStore.Intent, RegistrationStore.State, Nothing> {
    sealed class Intent {
        data class CreateMember(
            val token: String,
            val firstName: String,
            val lastName: String,
            val phoneNumber: String,
            val idNumber: String,
            val tocsAccepted: Boolean,
            val tenantId: String
        ): Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val registrationResponse: PrestaRegistrationResponse? = null,
        val label: String = "Let us know a bit more so we can help you get started.",
        val title: String = "Let's create the best experience for you",
        val inputs: List<InputMethod> = listOf(
            InputMethod(
                inputLabel = "First Name*",
                fieldType = InputFields.FIRST_NAME,
                inputTypes = InputTypes.STRING,
                required = true,
                errorMessage = ""
            ),
            InputMethod(
                inputLabel = "Last Name*",
                fieldType = InputFields.LAST_NAME,
                inputTypes = InputTypes.STRING,
                required = true,
                errorMessage = ""
            ),
            InputMethod(
                inputLabel = "Email*",
                fieldType = InputFields.EMAIL,
                inputTypes = InputTypes.STRING,
                required = true,
                errorMessage = ""
            ),
            InputMethod(
                inputLabel = "ID Number*",
                fieldType = InputFields.ID_NUMBER,
                inputTypes = InputTypes.NUMBER,
                required = true,
                errorMessage = ""
            ),
            InputMethod(
                inputLabel = "Gender",
                fieldType = InputFields.GENDER,
                inputTypes = InputTypes.STRING,
                required = true,
                errorMessage = ""
            ),
            InputMethod(
                inputLabel = "Introducer",
                fieldType = InputFields.INTRODUCER,
                inputTypes = InputTypes.STRING,
                required = true,
                errorMessage = ""
            ),
        ),
        val phoneNumber: String? = null,
        val isTermsAccepted: Boolean = true,
        val isActive: Boolean = true,
        val onBoardingContext: DefaultRootComponent.OnBoardingContext = DefaultRootComponent.OnBoardingContext.REGISTRATION,
    )
}