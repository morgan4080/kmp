package com.presta.customer.ui.signAppHome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultSignHomeComponent (
    componentContext: ComponentContext,
    private val onSelected: (item: String) -> Unit,
): SignHomeComponent, ComponentContext by componentContext {
    override val model: Value<SignHomeComponent.Model> =
        MutableValue(
            SignHomeComponent.Model(
                // create welcome screens here
                items = List(120) { "Country $it" }
            )
        )

    override fun onSelected(item: String) {
        onSelected(item)
    }
}