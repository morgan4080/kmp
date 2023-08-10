package com.presta.customer.ui.components.signDocument

import com.arkivanov.decompose.ComponentContext

class DefaultSignDocumentComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
    private val onProductClicked: () -> Unit,
    override val loanNumber: String,
    override val amount: Double,
    override val loanRequestRefId: String
): SignDocumentComponent, ComponentContext by componentContext {
    override fun onBackNavClicked() {
        onItemClicked()
    }

    override fun onProductSelected() {
      onProductClicked()
    }
}