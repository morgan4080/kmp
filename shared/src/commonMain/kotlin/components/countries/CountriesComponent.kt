package components.countries

import com.arkivanov.decompose.value.Value
import dev.icerock.moko.resources.FileResource

interface CountriesComponent {
    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val countriesJSON: FileResource,
    )
}