package com.presta.customer.ui.components.payLoanPropmpt

import com.arkivanov.decompose.value.Value

interface PayLoanPromptComponent {

    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )
}