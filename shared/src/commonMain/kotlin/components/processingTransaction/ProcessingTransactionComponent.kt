package components.processingTransaction

import com.arkivanov.decompose.value.Value

interface ProcessingTransactionComponent {

    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )

}