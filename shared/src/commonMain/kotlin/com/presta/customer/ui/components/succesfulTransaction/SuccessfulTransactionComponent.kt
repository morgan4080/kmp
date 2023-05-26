package com.presta.customer.ui.components.succesfulTransaction

import com.arkivanov.decompose.value.Value

interface SuccessfulTransactionComponent {

    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )
}