package com.presta.customer.ui.components.addSavings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultAddSavingsComponent (
    componentContext: ComponentContext,
    private val onConfirmClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,
): AddSavingsComponent,ComponentContext by componentContext{
    override fun onConfirmSelected(mode: SavingsModes, amount: Double) {
       onConfirmClicked()
    }

    override fun onBackNavSelected() {
        onBackNavClicked()
    }
}