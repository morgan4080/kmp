package components.onboarding

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import components.countries.Country

class DefaultOnboardingComponent (
    componentContext: ComponentContext,
    country: String,
    private val onSubmitClicked: () -> Unit,
    private val onSelectCountryClicked: () -> Unit,
    private val onSelectOrganisationClicked: () -> Unit
): OnboardingComponent, ComponentContext by componentContext {
    override val model: Value<OnboardingComponent.Model> =
        MutableValue(
            OnboardingComponent.Model(
                inputs = listOf(
                    OnboardingComponent.InputMethod(
                        inputLabel = "Organisation",
                        fieldType = OnboardingComponent.InputFields.ORGANISATION,
                        valueType = String,
                        errorMessage = ""
                    ),
                    OnboardingComponent.InputMethod(
                        inputLabel = "Country",
                        fieldType = OnboardingComponent.InputFields.COUNTRY,
                        valueType = Int,
                        errorMessage = ""
                    ),
                    OnboardingComponent.InputMethod(
                        inputLabel = "Phone number",
                        fieldType = OnboardingComponent.InputFields.PHONE_NUMBER,
                        valueType = Int,
                        errorMessage = ""
                    )
                ),
                label = "Start your digital lending journey here.",
                title = "Get your loans appraised digitally from anywhere, anytime.",
                country = country
            )
        )
    override fun onSubmit() {
        onSubmitClicked()
    }

    override fun onSelectCountry() {
       onSelectCountryClicked()
    }

    override fun onSelectOrganisation() {
        onSelectOrganisationClicked()
    }

    override fun onCountrySelected(country: String) {
        println(country)
    }

}