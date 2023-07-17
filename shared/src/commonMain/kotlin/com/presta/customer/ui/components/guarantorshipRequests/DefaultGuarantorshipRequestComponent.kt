package com.presta.customer.ui.components.guarantorshipRequests

import com.arkivanov.decompose.ComponentContext

class DefaultGuarantorshipRequestComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
    private val onProductClicked: () -> Unit
): GuarantorshipRequestComponent, ComponentContext by componentContext {
    override fun onBackNavClicked() {
        onItemClicked()
    }

    override fun onProductSelected() {
      onProductClicked()
    }
}