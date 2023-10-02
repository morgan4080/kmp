package com.presta.customer.ui.components.signAppHome.store

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.signHome.data.SignHomeRepository
import com.presta.customer.network.signHome.model.PrestaSignUserDetailsResponse
import com.presta.customer.prestaDispatchers
import com.presta.customer.ui.components.registration.store.InputFields
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignHomeStoreFactory(
    private val storeFactory: StoreFactory

) : KoinComponent {
    private val signHomeRepository by inject<SignHomeRepository>()

    fun create(): SignHomeStore =
        object : SignHomeStore,
            Store<SignHomeStore.Intent, SignHomeStore.State, Nothing> by storeFactory.create(
                name = "SignHomeStore",
                initialState = SignHomeStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {
        data class SignHomeLoading(val isLoading: Boolean = true) : Msg()
        data class SignTenantByPhoneNumberLoaded(val signTenantById: PrestaSignUserDetailsResponse) :
            Msg()

        data class SignTenantByMemberNumberLoaded(val signTenantByMemberNumber: PrestaSignUserDetailsResponse) :
            Msg()

        data class UpdateSignTenantDetails(val updateSignTenantDetails: PrestaSignUserDetailsResponse) :
            Msg()

        data class UpdateSignTenanPersonalInfo(val updateSignTenantPersonalInfo: PrestaSignUserDetailsResponse) :
            Msg()

        object ClearError : Msg()
        data class UpdateInputValue(val inputField: InputFields, val value: TextFieldValue) : Msg()
        object ClearKycError : Msg()
        data class UpdateKycValue(val inputField: KycInputs, val value: TextFieldValue) : Msg()

        data class SignHomeFailed(val error: String?) : Msg()

    }

    private inner class ExecutorImpl :
        CoroutineExecutor<SignHomeStore.Intent, Unit, SignHomeStore.State, SignHomeStoreFactory.Msg, Nothing>(
            prestaDispatchers.main
        ) {
        override fun executeAction(action: Unit, getState: () -> SignHomeStore.State) {

        }

        override fun executeIntent(
            intent: SignHomeStore.Intent, getState: () -> SignHomeStore.State
        ): Unit =
            when (intent) {

                is SignHomeStore.Intent.GetPrestaTenantByPhoneNumber -> getPrestaTenantByPhoneNumber(
                    token = intent.token,
                    phoneNumber = intent.phoneNumber
                )

                is SignHomeStore.Intent.GetPrestaTenantByMemberNumber -> getPrestaTenantByMemberNumber(
                    token = intent.token,
                    memberNumber = intent.memberNumber
                )

                is SignHomeStore.Intent.UpdatePrestaTenantDetails -> updatePrestaMemberDetails(
                    token = intent.token,
                    memberRefId = intent.memberRefId,
                    details = intent.details
                )

                is SignHomeStore.Intent.UpdatePrestaTenantPersonalInfo -> updatePrestaMemberPersonalInfo(
                    token = intent.token,
                    memberRefId = intent.memberRefId,
                    firstName = intent.firstName,
                    lastName = intent.lastName,
                    phoneNumber = intent.phoneNumber,
                    idNumber = intent.idNumber,
                    email = intent.email,
                    callback = intent.callback
                )

                is SignHomeStore.Intent.UpdateInputValue ->
                    dispatch(
                        Msg.UpdateInputValue(
                            inputField = intent.inputField,
                            value = intent.value
                        )
                    )

                is SignHomeStore.Intent.ClearError ->
                    dispatch(Msg.ClearError)

                is SignHomeStore.Intent.UpdateKycValues ->
                    dispatch(
                        Msg.UpdateKycValue(
                            inputField = intent.inputField,
                            value = intent.value
                        )
                    )

                is SignHomeStore.Intent.ClearKYCErrors ->
                    dispatch(Msg.ClearKycError)

            }

        private var getPrestaTenantByPhoneNumberJob: Job? = null

        private fun getPrestaTenantByPhoneNumber(
            token: String,
            phoneNumber: String
        ) {
            if (getPrestaTenantByPhoneNumberJob?.isActive == true) return

            dispatch(Msg.SignHomeLoading())

            getPrestaTenantByPhoneNumberJob = scope.launch {
                signHomeRepository.getTenantByPhoneNumber(
                    token = token,
                    phoneNumber = phoneNumber
                ).onSuccess { response ->
                    dispatch(Msg.SignTenantByPhoneNumberLoaded(response))
                    //use  the response to push the update
                    dispatch(
                        Msg.UpdateInputValue(
                            inputField = InputFields.FIRST_NAME,
                            value = TextFieldValue(response.firstName)
                        )
                    )
                    dispatch(
                        Msg.UpdateInputValue(
                            inputField = InputFields.LAST_NAME,
                            value = TextFieldValue(response.lastName)
                        )
                    )
                    dispatch(
                        Msg.UpdateInputValue(
                            inputField = InputFields.EMAIL,
                            value = TextFieldValue(response.email)
                        )
                    )
                    dispatch(
                        Msg.UpdateInputValue(
                            inputField = InputFields.ID_NUMBER,
                            value = TextFieldValue(response.idNumber)
                        )
                    )
                    dispatch(
                        Msg.UpdateInputValue(
                            inputField = InputFields.INTRODUCER,
                            value = TextFieldValue(response.phoneNumber)
                        )
                    )
                    //update
                    response.details?.map { it ->
                        if (it.key.contains("employer")) {
                            dispatch(
                                Msg.UpdateKycValue(
                                    inputField = KycInputs.EMPLOYER,
                                    value = TextFieldValue(it.value.value.toString())
                                )
                            )
                        }
                        if (it.key.contains("gross")) {
                            dispatch(
                                Msg.UpdateKycValue(
                                    inputField = KycInputs.GROSSSALARY,
                                    value = TextFieldValue(it.value.value.toString())
                                )
                            )
                        }
                        if (it.key.contains("net")) {
                            dispatch(
                                Msg.UpdateKycValue(
                                    inputField = KycInputs.NETSALARY,
                                    value = TextFieldValue(it.value.value.toString())
                                )
                            )

                        }
                        if (it.key.contains("kra")) {
                            dispatch(
                                Msg.UpdateKycValue(
                                    inputField = KycInputs.KRAPIN,
                                    value = TextFieldValue(it.value.value.toString())
                                )
                            )
                        }
                        if (it.key.contains("employment")) {
                            dispatch(
                                Msg.UpdateKycValue(
                                    inputField = KycInputs.EMPLOYMENTNUMBER,
                                    value = TextFieldValue(it.value.value.toString())
                                )
                            )

                        }
                        if (it.key.contains("businessLocation")) {
                            dispatch(
                                Msg.UpdateKycValue(
                                    inputField = KycInputs.BUSINESSLOCATION,
                                    value = TextFieldValue(it.value.value.toString())
                                )
                            )

                        }
                        if (it.key.contains("businessType")) {
                            dispatch(
                                Msg.UpdateKycValue(
                                    inputField = KycInputs.BUSINESSTYPE,
                                    value = TextFieldValue(it.value.value.toString())
                                )
                            )
                        }
                    }

                }.onFailure { e ->
                    dispatch(Msg.SignHomeFailed(e.message))
                }
                dispatch(Msg.SignHomeLoading(false))
            }
        }

        private var getPrestaTenantByMemberNumberJob: Job? = null
        private fun getPrestaTenantByMemberNumber(
            token: String,
            memberNumber: String
        ) {
            if (getPrestaTenantByMemberNumberJob?.isActive == true) return

            dispatch(Msg.SignHomeLoading())

            getPrestaTenantByMemberNumberJob = scope.launch {
                signHomeRepository.getTenantByMemberNumber(
                    token = token,
                    memberNumber = memberNumber
                ).onSuccess { response ->
                    dispatch(Msg.SignTenantByMemberNumberLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.SignHomeFailed(e.message))
                }
                dispatch(Msg.SignHomeLoading(false))
            }
        }

        private var updatePrestaMemberDetailsJob: Job? = null
        private fun updatePrestaMemberDetails(
            token: String,
            memberRefId: String,
            details: MutableMap<String, String>
        ) {
            if (updatePrestaMemberDetailsJob?.isActive == true) return

            dispatch(Msg.SignHomeLoading())

            updatePrestaMemberDetailsJob = scope.launch {
                signHomeRepository.upDateMemberDetails(
                    token = token,
                    memberRefId = memberRefId,
                    details = details
                ).onSuccess { response ->
                    dispatch(Msg.UpdateSignTenantDetails(response))
                }.onFailure { e ->
                    dispatch(Msg.SignHomeFailed(e.message))
                }
                dispatch(Msg.SignHomeLoading(false))
            }
        }

        private var updatePrestaMemberPersonalInfoJob: Job? = null
        private fun updatePrestaMemberPersonalInfo(
            token: String,
            memberRefId: String,
            firstName: String,
            lastName: String,
            phoneNumber: String,
            idNumber: String,
            email: String,
            callback: () -> Unit
        ) {
            if (updatePrestaMemberPersonalInfoJob?.isActive == true) return

            dispatch(Msg.SignHomeLoading())

            updatePrestaMemberPersonalInfoJob = scope.launch {
                signHomeRepository.upDateMemberPersonalInfo(
                    token = token,
                    memberRefId = memberRefId,
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    idNumber = idNumber,
                    email = email
                ).onSuccess { response ->
                    dispatch(Msg.UpdateSignTenantDetails(response))
                    callback()
                }.onFailure { e ->
                    dispatch(Msg.SignHomeFailed(e.message))
                }
                dispatch(Msg.SignHomeLoading(false))
            }
        }
    }

    private object ReducerImpl :
        Reducer<SignHomeStore.State, Msg> {
        override fun SignHomeStore.State.reduce(msg: Msg): SignHomeStore.State =
            when (msg) {
                is Msg.SignHomeLoading -> copy(isLoading = msg.isLoading)
                is Msg.SignHomeFailed -> copy(error = msg.error)
                is Msg.SignTenantByPhoneNumberLoaded -> copy(prestaTenantByPhoneNumber = msg.signTenantById)
                is Msg.SignTenantByMemberNumberLoaded -> copy(prestaTenantByMemberNumber = msg.signTenantByMemberNumber)
                is Msg.UpdateSignTenantDetails -> copy(updatePrestaTenantDetails = msg.updateSignTenantDetails)
                is Msg.UpdateSignTenanPersonalInfo -> copy(updatePrestaTenantPersonalInfo = msg.updateSignTenantPersonalInfo)
                is Msg.UpdateInputValue -> {
                    when (msg.inputField) {
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
                                errorMsg = "Phone number is required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Not a valid Phone number"
                                }
                            }
                            copy(
                                introducer = introducer.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }

                        InputFields.EMAIL -> {
                            // validate email
                            val pattern =
                                Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~\\-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~\\-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")
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
                    }
                }
                //update
                is Msg.UpdateKycValue -> {
                    when (msg.inputField) {
                        KycInputs.EMPLOYER -> {
                            // validate first name
                            val pattern = Regex("^(\\s*[a-zA-Z\\s]*)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && employer.required) {
                                errorMsg = "employer is required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "enter valid value."
                                }
                            }
                            copy(
                                employer = employer.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }
                        KycInputs.EMPLOYMENTTERMS -> {
                            // validate first name
                            val pattern = Regex("^(\\s*[a-zA-Z\\s]*)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && employmentType.required) {
                                errorMsg = "employment terms is required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "enter valid value."
                                }
                            }
                            copy(
                                employmentType = employmentType.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }
                        KycInputs.DEPARTMENT -> {
                            // validate first name
                            val pattern = Regex("^(\\s*[a-zA-Z\\s]*)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && department.required) {
                                errorMsg = "Department is required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "enter valid value."
                                }
                            }
                            copy(
                                department = department.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }

                        KycInputs.EMPLOYMENTNUMBER -> {
                            // validate last name
                            val pattern = Regex("^([a-zA-Z0-9]+)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && employmentNumber.required) {
                                errorMsg = "Employment Number required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Not a valid employment Number."
                                }
                            }
                            copy(
                                employmentNumber = employmentNumber.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }

                        KycInputs.GROSSSALARY -> {
                            // validate id number
                            val pattern = Regex("^([0-9]+)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && grossSalary.required) {
                                errorMsg = "Gross Salary required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Enter a valid value"
                                }
                            }
                            copy(
                                grossSalary = grossSalary.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }

                        KycInputs.NETSALARY -> {
                            // validate introducer
                            val pattern = Regex("^([0-9]+)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && netSalary.required) {
                                errorMsg = "Net Salary required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Enter a valid value"
                                }
                            }
                            copy(
                                netSalary = netSalary.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }

                        KycInputs.KRAPIN -> {
                            // validate introducer
                            val pattern = Regex("^([a-zA-Z0-9]+)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && kraPin.required) {
                                errorMsg = "Kra Pin required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Enter a valid value"
                                }
                            }
                            copy(
                                kraPin = kraPin.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }

                        KycInputs.BUSINESSLOCATION -> {
                            // validate first name
                            val pattern = Regex("^(\\s*[a-zA-Z\\s]*)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && businessLocation.required) {
                                errorMsg = "Business Location required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Enter a  valid value."
                                }
                            }
                            copy(
                                businessLocation = businessLocation.copy(
                                    value = msg.value,
                                    errorMessage = errorMsg,
                                )
                            )
                        }

                        KycInputs.BUSINESSTYPE -> {
                            val pattern = Regex("^(\\s*[a-zA-Z\\s]*)")
                            var errorMsg = ""
                            if (msg.value.text.isEmpty() && businessType.required) {
                                errorMsg = "Business Type Required"
                            } else {
                                if (!msg.value.text.matches(pattern)) {
                                    errorMsg = "Enter a valid value."
                                }
                            }
                            copy(
                                businessType = businessType.copy(
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

                is Msg.ClearKycError -> copy(
                    error = null
                )

            }
    }


}