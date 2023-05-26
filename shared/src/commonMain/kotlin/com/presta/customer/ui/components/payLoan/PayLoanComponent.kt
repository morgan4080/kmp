package com.presta.customer.ui.components.payLoan

import com.arkivanov.decompose.value.Value

interface PayLoanComponent {

    val model: Value<Model>

    fun onPaySelected(refId: String)

    data class Model(
        val items: List<String>,
    )


}