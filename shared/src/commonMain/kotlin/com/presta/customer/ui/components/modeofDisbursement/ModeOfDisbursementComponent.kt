package com.presta.customer.ui.components.modeofDisbursement

import com.arkivanov.decompose.value.Value

interface ModeOfDisbursementComponent {
    val model: Value<Model>

    fun onMpesaSelected()
    fun onBankSelected()
    fun onBackNavSelected()
    fun successFulTransaction()

    data class Model(
        val items: List<String>,
    )


}