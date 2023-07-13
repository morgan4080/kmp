package com.presta.customer.ui.components.selectLoanPurpose

import com.arkivanov.decompose.ComponentContext

class DefaultSelectLoanPurposeComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
    private val onContinueClicked: () -> Unit
): SelectLoanPurposeComponent, ComponentContext by componentContext {
    override fun onBackNavClicked() {
        onItemClicked()
    }

    override fun onContinueSelected() {
     onContinueClicked()
    }
}