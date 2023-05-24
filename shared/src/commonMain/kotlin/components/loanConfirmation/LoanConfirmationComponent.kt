package components.loanConfirmation

import com.arkivanov.decompose.value.Value

interface LoanConfirmationComponent {

    val model: Value<Model>

    fun onConfirmSelected()
    fun onBackNavSelected()

    data class Model(
        val items: List<String>,
    )
}