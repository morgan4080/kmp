package components.shortTermLoans

import com.arkivanov.decompose.value.Value

interface ShortTermLoansComponent {

    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )

}