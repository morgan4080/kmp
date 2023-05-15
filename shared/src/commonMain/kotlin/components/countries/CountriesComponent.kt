package components.countries

import com.arkivanov.decompose.value.Value
import dev.icerock.moko.resources.FileResource
import kotlinx.serialization.Serializable


@Serializable
data class Country(val name: String, val code: String, val alpha2Code: String, val numericCode: String)
interface CountriesComponent {
    val model: Value<Model>

    fun onSelected(country: String)
    fun onBack()

    data class Model(
        val countriesJSON: FileResource,
    )
}