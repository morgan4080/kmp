package com.presta.customer.ui.components.sign

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultSignComponent (
    componentContext: ComponentContext,
<<<<<<< HEAD:shared/src/commonMain/kotlin/components/savings/DefaultSavingsComponent.kt
    private val onAddSavingsClicked: () -> Unit,
    private val onSeeAlClicked: () -> Unit,

): SavingsComponent, ComponentContext by componentContext {
    override val model: Value<SavingsComponent.Model> =
=======
    private val onSelected: (item: String) -> Unit,
): SignComponent, ComponentContext by componentContext {
    override val model: Value<SignComponent.Model> =
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/components/sign/DefaultSignComponent.kt
        MutableValue(
            SignComponent.Model(
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