package com.presta.customer.ui.components.applyLongTermLoan

import com.arkivanov.decompose.ComponentContext

class DefaultApplyLongtermLoanComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
): ApplyLongTermLoanComponent, ComponentContext by componentContext {
    override fun onSelected() {
        onItemClicked()
    }
}