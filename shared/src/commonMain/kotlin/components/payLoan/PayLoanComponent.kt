package components.payLoan

import com.arkivanov.decompose.value.Value

interface PayLoanComponent {

    val model: Value<Model>

    fun onSelected(refId: String)

    data class Model(
        val items: List<String>,
    )


}