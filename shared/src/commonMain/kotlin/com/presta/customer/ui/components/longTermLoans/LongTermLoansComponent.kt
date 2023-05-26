package com.presta.customer.ui.components.longTermLoans

import com.arkivanov.decompose.value.Value

interface LongTermLoansComponent {

    val model: Value<Model>

    fun onSelected(refId: String)

    data class Model(
        val items: List<String>,
    )

}