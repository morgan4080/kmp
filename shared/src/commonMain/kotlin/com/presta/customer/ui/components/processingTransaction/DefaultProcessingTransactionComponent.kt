package com.presta.customer.ui.components.processingTransaction

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultProcessingTransactionComponent(
    componentContext: ComponentContext,

): ProcessingTransactionComponent,ComponentContext by componentContext {

    private val models = MutableValue(
        ProcessingTransactionComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<ProcessingTransactionComponent.Model> = models

    override fun onSelected(item: String) {
        TODO("Not yet implemented")
    }

}