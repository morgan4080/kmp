import com.arkivanov.decompose.value.Value

interface ApplyLoanComponent {
    val model: Value<Model>

    fun onLoanSelected()

    data class Model(
        val items: List<String>,
    )
}