import com.arkivanov.decompose.value.Value

interface FailedTransactionComponent {
    val model: Value<Model>

    fun onRetrySelected()

    data class Model(
        val items: List<String>,
    )
}