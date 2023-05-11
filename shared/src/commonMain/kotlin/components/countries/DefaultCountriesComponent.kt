package components.countries

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import prestaCustomer.shared.MR

class DefaultCountriesComponent(
    componentContext: ComponentContext,
    private val onSelected: (item: String) -> Unit,
) : CountriesComponent, ComponentContext by componentContext {
    override val model: Value<CountriesComponent.Model> =
        MutableValue(
            CountriesComponent.Model(
                countriesJSON = MR.files.Countries
            )
        )

    override fun onSelected(item: String) {
        onSelected(item)
    }
}