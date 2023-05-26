package com.presta.customer.ui.components.emergencyLoans

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultEmergencyLoansComponent (
    componentContext: ComponentContext,
    private val onConfirmClicked: () -> Unit,
    private val onBackNavClicked: () -> Unit,

    ): EmergencyLoansComponent, ComponentContext by componentContext {
    private val models = MutableValue(
        EmergencyLoansComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<EmergencyLoansComponent.Model> =models
    override fun onConfirmSelected() {
        onConfirmClicked()
    }

    override fun onBackNavSelected() {
       onBackNavClicked()
    }

}