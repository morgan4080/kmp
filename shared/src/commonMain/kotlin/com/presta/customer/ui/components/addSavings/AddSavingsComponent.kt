package com.presta.customer.ui.components.addSavings

import com.arkivanov.decompose.value.Value

interface AddSavingsComponent {
    fun onConfirmSelected(mode: SavingsModes, amount: Double)
    fun onBackNavSelected()
}