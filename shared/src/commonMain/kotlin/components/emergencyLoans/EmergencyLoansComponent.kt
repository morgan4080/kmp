package components.emergencyLoans

import com.arkivanov.decompose.value.Value

interface EmergencyLoansComponent {

    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )



}