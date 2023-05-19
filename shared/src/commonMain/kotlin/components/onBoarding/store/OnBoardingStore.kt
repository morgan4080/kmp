package components.onBoarding.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.MR
import components.root.DefaultRootComponent
import dev.icerock.moko.resources.FileResource
import organisation.Organisation
import organisation.OrganisationModel

enum class InputFields {
    ORGANISATION,
    COUNTRY,
    PHONE_NUMBER,
}

data class InputMethod(val inputLabel: String, val fieldType: InputFields, val valueType: Any, val errorMessage: String)

enum class IdentifierTypes {
    PHONE_NUMBER,
    EMAIL,
    ID_NUMBER
}
interface OnBoardingStore: Store<OnBoardingStore.Intent, OnBoardingStore.State, Nothing> {
    sealed class Intent {
        data class GetMemberDetails(val token: String, val memberIdentifier: String, val identifierType: IdentifierTypes): Intent()
        data class UpdateMember(val token: String, val memberRefId: String, val pinConfirmation: String): Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val phoneNumber: String = "",
        val inputs: List<InputMethod> = listOf(
            InputMethod(
                inputLabel = "Organisation",
                fieldType = InputFields.ORGANISATION,
                valueType = String,
                errorMessage = ""
            ),
            InputMethod(
                inputLabel = "Country",
                fieldType = InputFields.COUNTRY,
                valueType = Int,
                errorMessage = ""
            ),
            InputMethod(
                inputLabel = "Phone number",
                fieldType = InputFields.PHONE_NUMBER,
                valueType = Int,
                errorMessage = ""
            )
        ),
        val label: String = "Verify phone number to",
        val title: String = "Get Started",
        val countriesJSON: FileResource = MR.files.Countries,
        val country: String = "",
        val onBoardingContext: DefaultRootComponent.OnBoardingContext = DefaultRootComponent.OnBoardingContext.LOGIN,
        val organisation: Organisation = OrganisationModel.organisation
    )
}