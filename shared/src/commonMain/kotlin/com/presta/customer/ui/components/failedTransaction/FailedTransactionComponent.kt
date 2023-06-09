import com.arkivanov.decompose.value.Value

interface FailedTransactionComponent {
    val model: Value<Model>

<<<<<<< HEAD:shared/src/commonMain/kotlin/components/applyLoan/ApplyLoanComponent.kt
    fun onShortTermSelected()
    fun onLongTermSelected()
    fun onBackNavSelected()
=======
    fun onRetrySelected()
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/components/failedTransaction/FailedTransactionComponent.kt

    data class Model(
        val items: List<String>,
    )
}