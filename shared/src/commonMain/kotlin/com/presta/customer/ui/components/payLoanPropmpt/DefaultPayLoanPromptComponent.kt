package com.presta.customer.ui.components.payLoanPropmpt

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory

class DefaultPayLoanPromptComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    mainContext: ComponentContext,
):  PayLoanPromptComponent,ComponentContext by componentContext {

}