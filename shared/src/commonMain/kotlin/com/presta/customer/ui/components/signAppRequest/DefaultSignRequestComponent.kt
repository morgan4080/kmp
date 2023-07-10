package com.presta.customer.ui.components.signAppRequest

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultSignRequestComponent (
    componentContext: ComponentContext,
    private val onSelected: (item: String) -> Unit,
): SignRequestComponent, ComponentContext by componentContext {
    override val model: Value<SignRequestComponent.Model> =
        MutableValue(
            SignRequestComponent.Model(
                // create welcome screens here
                items = List(120) { "Country $it" }
            )
        )

    override fun onSelected(item: String) {
        onSelected(item)
    }
}