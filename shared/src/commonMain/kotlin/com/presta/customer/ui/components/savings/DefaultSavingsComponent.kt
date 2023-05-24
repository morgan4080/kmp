package com.presta.customer.ui.components.savings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultSavingsComponent (
    componentContext: ComponentContext,
    private val onSelected: (item: String) -> Unit,
): SavingsComponent, ComponentContext by componentContext {
    override val model: Value<SavingsComponent.Model> =
        MutableValue(
            SavingsComponent.Model(
                // create welcome screens here
                items = List(120) { "Country $it" }
            )
        )

    override fun onSelected(item: String) {
        onSelected(item)
    }
}