package com.presta.customer.ui.components.savingsTransactionHistory

import com.arkivanov.decompose.value.Value

interface SavingsTransactionHistoryComponent {
    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )


}