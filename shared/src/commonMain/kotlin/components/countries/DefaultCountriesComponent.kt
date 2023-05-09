package components.countries

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultCountriesComponent(
    componentContext: ComponentContext,
    private val onSelected: (item: String) -> Unit,
) : CountriesComponent, ComponentContext by componentContext {
    override val model: Value<CountriesComponent.Model> =
        MutableValue(
            CountriesComponent.Model(
            // create welcome screens here
            items = List(120) { "Country $it" }
            )
        )

    override fun onSelected(item: String) {
        onSelected(item)
    }
}