package components.addSavings

import com.arkivanov.decompose.value.Value

interface AddSavingsComponent {

    val model: Value<Model>

    fun onConfirmSelected()
    fun onBackNavSelected()

    data class Model(
        val items: List<String>,
    )


}