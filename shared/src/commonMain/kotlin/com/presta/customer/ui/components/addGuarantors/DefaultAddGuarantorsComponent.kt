package com.presta.customer.ui.components.addGuarantors

import com.arkivanov.decompose.ComponentContext
import com.presta.customer.AndroidContactsHandler
import com.presta.customer.Platform
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DefaultAddGuarantorsComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
    private val onContinueClicked: () -> Unit,

): AddGuarantorsComponent, ComponentContext by componentContext , KoinComponent {

    override val platform by inject<Platform>()
    override fun onBackNavClicked() {
        onItemClicked()
    }
    override fun onContinueSelected() {
      onContinueClicked()
    }
}