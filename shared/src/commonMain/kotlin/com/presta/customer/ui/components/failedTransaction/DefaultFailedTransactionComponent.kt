package com.presta.customer.ui.components.failedTransaction

import FailedTransactionComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultFailedTransactionComponent(
    componentContext: ComponentContext,
    private val onRetryClicked: () -> Unit,
): FailedTransactionComponent , ComponentContext by componentContext {
    override val model: Value<FailedTransactionComponent.Model> =
        MutableValue(
            FailedTransactionComponent.Model(
                items = List(120) { "Profile $it" }
            )
        )

    override fun onRetrySelected() {
        onRetryClicked()

    }


}