package components.onboarding

import com.arkivanov.decompose.value.Value
import components.countries.Country


interface OnboardingComponent {
    val model: Value<Model>

    fun onSubmit()

    fun onSelectCountry()

    fun onSelectOrganisation()

    fun onCountrySelected(country: String)

    data class Model(
        val inputs: List<InputMethod>,
        val label: String,
        val title: String,
        val country: String
    )

    enum class InputFields {
        ORGANISATION,
        COUNTRY,
        PHONE_NUMBER,
    }

    data class InputMethod(val inputLabel: String, val fieldType: InputFields, val valueType: Any, val errorMessage: String)
}