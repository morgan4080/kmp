package components.savings

import com.arkivanov.decompose.value.Value

interface SavingsComponent {
    val model: Value<Model>
    fun onAddSavingsSelected()
    fun onSeeALlSelected()

    data class Model(
        val items: List<String>,
    )
}