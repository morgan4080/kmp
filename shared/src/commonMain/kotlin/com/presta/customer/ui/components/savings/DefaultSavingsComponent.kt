package com.presta.customer.ui.components.savings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultSavingsComponent (
    componentContext: ComponentContext,
    private val onAddSavingsClicked: () -> Unit,
    private val onSeeAlClicked: () -> Unit,

): SavingsComponent, ComponentContext by componentContext {
    override val model: Value<SavingsComponent.Model> =
        MutableValue(
            SavingsComponent.Model(
                // create welcome screens here
                items = List(120) { "Country $it" }
            )
        )

    override fun onAddSavingsSelected() {
       onAddSavingsClicked()
    }

    override fun onSeeALlSelected() {
       onSeeAlClicked()
    }


}