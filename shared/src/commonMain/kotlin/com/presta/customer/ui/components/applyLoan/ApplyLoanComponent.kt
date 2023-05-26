import com.arkivanov.decompose.value.Value

interface ApplyLoanComponent {
    val model: Value<Model>

    fun onShortTermSelected()
    fun onLongTermSelected()
    fun onBackNavSelected()

    data class Model(
        val items: List<String>,
    )
}