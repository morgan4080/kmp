package com.presta.customer.ui.components.banKDisbursement

import com.arkivanov.decompose.value.Value

interface BankDisbursementComponent {

    val model: Value<Model>

    fun onConfirmSelected()
    fun onBackNavSelected()

    data class Model(
        val items: List<String>,
    )
}