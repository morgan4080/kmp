package com.presta.customer.ui.components.payLoanPropmpt

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultPayLoanPromptComponent(
    componentContext: ComponentContext,
):  PayLoanPromptComponent,ComponentContext by componentContext {

    private val models = MutableValue(
        PayLoanPromptComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<PayLoanPromptComponent.Model> = models
    override fun onSelected(item: String) {
        TODO("Not yet implemented")
    }

}