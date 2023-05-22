package components.shortTermLoans

import com.arkivanov.decompose.value.Value

interface ShortTermLoansComponent {

    val model: Value<Model>

    fun onSelected(refId: String)
    fun onSelecte2(refId: String)
    fun onConfirmSelected(refId: String)


    data class Model(
        val items: List<String>,
    )

}