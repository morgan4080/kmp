package com.presta.customer.ui.components.longTermLoanConfirmation

import com.arkivanov.decompose.ComponentContext

class DefaultLongTermLoanConfirmationComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
    private val onProductClicked: () -> Unit
): LongTermLoanConfirmationComponent, ComponentContext by componentContext {
    override fun onBackNavClicked() {
        onItemClicked()
    }

    override fun onProductSelected() {
      onProductClicked()
    }
}