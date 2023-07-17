package com.presta.customer.ui.components.witnessRequests

import com.arkivanov.decompose.ComponentContext

class DefaultWitnessRequestComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
    private val onProductClicked: () -> Unit
): WitnessRequestComponent, ComponentContext by componentContext {
    override fun onBackNavClicked() {
        onItemClicked()
    }

    override fun onProductSelected() {
      onProductClicked()
    }
}