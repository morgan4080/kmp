package com.presta.customer.ui.components.loanProduct

import com.arkivanov.decompose.value.Value

interface ProductComponent {

    val model: Value<Model>

    fun onEmergencyLoanSelected()

    data class Model(
        val items: List<String>,
        val refId: String
    )


}