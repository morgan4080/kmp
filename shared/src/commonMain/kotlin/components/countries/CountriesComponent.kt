package components.countries

import com.arkivanov.decompose.value.Value

interface CountriesComponent {
    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )
}