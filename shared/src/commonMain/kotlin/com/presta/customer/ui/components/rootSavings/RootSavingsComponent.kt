package com.presta.customer.ui.components.rootSavings

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.addSavings.AddSavingsComponent
import com.presta.customer.ui.components.processingTransaction.ProcessingTransactionComponent
import com.presta.customer.ui.components.savings.SavingsComponent
import com.presta.customer.ui.components.savingsTransactionHistory.SavingsTransactionHistoryComponent

interface RootSavingsComponent {
    val childSavingsStack:Value<ChildStack<*, ChildSavings>>

    sealed class  ChildSavings {
        class SavingsHomeChild(val component: SavingsComponent): ChildSavings()
        class AddSavingsChild(val component: AddSavingsComponent): ChildSavings()
        class ProcessingTransactionChild(val component: ProcessingTransactionComponent): ChildSavings()
        class TransactionHistoryChild(val component: SavingsTransactionHistoryComponent): ChildSavings()
    }
}