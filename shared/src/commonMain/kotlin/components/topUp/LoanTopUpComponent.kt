package components.topUp

import com.arkivanov.decompose.value.Value

interface LoanTopUpComponent {

    val model: Value<Model>

    fun onConfirmSelected()
    fun onBackNavSelected()

    data class Model(
        val items: List<String>,
    )

}