package com.presta.customer.ui.components.replaceGuarantor

import com.arkivanov.decompose.ComponentContext

class DefaultReplaceGuarantorComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
    private val onProductClicked: () -> Unit
): ReplaceGuarantorComponent, ComponentContext by componentContext {
    override fun onBackNavClicked() {
        onItemClicked()
    }
    override fun onProductSelected() {
      onProductClicked()
    }
}