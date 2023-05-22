package components.profile

import com.arkivanov.decompose.value.Value

interface ProfileComponent {
    val model: Value<Model>

    fun onProfileSelected()
    fun onApplyLoanSelected()
    fun onAddSavingsSelected()
    fun onPayLoanSelected()
    fun onViewFullStatementSelected()

    data class Model(
        val items: List<String>,
    )
}