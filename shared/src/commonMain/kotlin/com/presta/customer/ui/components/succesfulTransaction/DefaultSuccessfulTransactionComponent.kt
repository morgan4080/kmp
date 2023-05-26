package com.presta.customer.ui.components.succesfulTransaction

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultSuccessfulTransactionComponent(
    componentContext: ComponentContext,

): SuccessfulTransactionComponent,ComponentContext by componentContext {

    private val models = MutableValue(
        SuccessfulTransactionComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<SuccessfulTransactionComponent.Model> = models

    override fun onSelected(item: String) {
        TODO("Not yet implemented")
    }

}