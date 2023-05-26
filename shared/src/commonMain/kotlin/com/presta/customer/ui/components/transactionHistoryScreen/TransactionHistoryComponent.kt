package com.presta.customer.ui.components.transactionHistoryScreen

import com.arkivanov.decompose.value.Value

interface TransactionHistoryComponent {
    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )


}