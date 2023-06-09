package com.presta.customer.ui.components.longTermLoans

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultLongTermComponent(
    componentContext: ComponentContext,
    private val onProductSelected: (refId: String) -> Unit,
): LongTermLoansComponent, ComponentContext by componentContext {

    private val models = MutableValue(
        LongTermLoansComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<LongTermLoansComponent.Model> = models

    override fun onSelected(refId: String) {
        onProductSelected(refId)
    }


}