package components.emergencyLoans

import com.arkivanov.decompose.value.Value

interface EmergencyLoansComponent {

    val model: Value<Model>

    fun onConfirmSelected()
    fun onBackNavSelected()

    data class Model(
        val items: List<String>,
    )



}