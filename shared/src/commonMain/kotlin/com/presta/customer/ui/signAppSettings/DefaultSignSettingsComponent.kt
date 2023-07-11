package com.presta.customer.ui.signAppSettings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultSignSettingsComponent (
    componentContext: ComponentContext,
    private val onSelected: (item: String) -> Unit,
): SignSettingsComponent, ComponentContext by componentContext {
    override val model: Value<SignSettingsComponent.Model> =
        MutableValue(
            SignSettingsComponent.Model(
                // create welcome screens here
                items = List(120) { "Country $it" }
            )
        )

    override fun onSelected(item: String) {
        onSelected(item)
    }
}