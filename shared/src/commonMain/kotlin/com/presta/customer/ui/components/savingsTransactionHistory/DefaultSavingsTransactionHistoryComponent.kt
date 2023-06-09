package com.presta.customer.ui.components.savingsTransactionHistory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultSavingsTransactionHistoryComponent(

    componentContext: ComponentContext,

) : SavingsTransactionHistoryComponent, ComponentContext by componentContext {

    private val models = MutableValue(
        SavingsTransactionHistoryComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<SavingsTransactionHistoryComponent.Model> = models
    override fun onSelected(item: String) {
        TODO("Not yet implemented")
    }


}