package components.banKDisbursement

import com.arkivanov.decompose.value.Value

interface BankDisbursementComponent {

    val model: Value<Model>

    fun onConfirmSelected()

    data class Model(
        val items: List<String>,
    )
}