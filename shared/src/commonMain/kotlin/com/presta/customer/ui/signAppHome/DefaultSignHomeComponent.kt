package com.presta.customer.ui.signAppHome

import com.arkivanov.decompose.ComponentContext

class DefaultSignHomeComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
): SignHomeComponent, ComponentContext by componentContext {
    override fun onSelected() {
        onItemClicked()
    }
}