package components.onBoarding

import com.arkivanov.decompose.value.Value
import organisation.Organisation


interface OnBoardingComponent {
    val model: Value<Model>

    fun onSubmit(
        organisation: Organisation,
        phone_number: String?,
        email: String?
    )

    fun onSelectCountry()

    fun onSelectOrganisation()

    fun onCountrySelected(country: String)

    data class Model(
        val loading: Boolean,
        val inputs: List<InputMethod>,
        val label: String,
        val title: String,
        val country: String,
        val organisation: Organisation,
        val errorMessage: String?,
    )

    enum class InputFields {
        ORGANISATION,
        COUNTRY,
        PHONE_NUMBER,
    }

    data class InputMethod(val inputLabel: String, val fieldType: InputFields, val valueType: Any, val errorMessage: String)
}