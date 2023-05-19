package components.savings

import com.arkivanov.decompose.value.Value

interface SavingsComponent {
    val model: Value<Model>
    fun onAddSavingsSelected()

    data class Model(
        val items: List<String>,
    )
}