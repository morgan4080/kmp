package com.presta.customer.ui.components.transactionHistoryScreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultTransactionHistoryComponent(

    componentContext: ComponentContext,

    ) : TransactionHistoryComponent, ComponentContext by componentContext {

    private val models = MutableValue(
        TransactionHistoryComponent.Model(
            items = listOf()
        )
    )

    override val model: Value<TransactionHistoryComponent.Model> = models
    override fun onSelected(item: String) {
        TODO("Not yet implemented")
    }


}