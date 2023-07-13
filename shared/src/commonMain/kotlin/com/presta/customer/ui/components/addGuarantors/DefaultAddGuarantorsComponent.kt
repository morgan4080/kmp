package com.presta.customer.ui.components.addGuarantors

import com.arkivanov.decompose.ComponentContext

class DefaultAddGuarantorsComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
    private val onProductClicked: () -> Unit
): AddGuarantorsComponent, ComponentContext by componentContext {
    override fun onBackNavClicked() {
        onItemClicked()
    }

    override fun onProductSelected() {
      onProductClicked()
    }
}