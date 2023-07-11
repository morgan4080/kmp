package com.presta.customer.ui.components.longTermLoanDetails

import com.arkivanov.decompose.ComponentContext

class DefaultLongTermLoanDetailsComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
    private val onProductClicked: () -> Unit,

): LongTermLoanDetailsComponent, ComponentContext by componentContext {
    override fun onBackNavClicked() {
        onItemClicked()
    }

    override fun onProductClicked() {
        onProductClicked()
    }
}