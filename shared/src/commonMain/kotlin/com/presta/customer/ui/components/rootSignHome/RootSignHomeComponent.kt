package com.presta.customer.ui.components.rootSignHome

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.savingsTransactionHistory.SavingsTransactionHistoryComponent
import com.presta.customer.ui.components.signAppHome.SignHomeComponent

interface RootSignHomeComponent {
    val childSignHomeStack:Value<ChildStack<*, ChildHomeSign>>
    fun applyLongTermLoan()

    sealed class  ChildHomeSign {
        class SignHomeChild(val component: SignHomeComponent): ChildHomeSign()
        class TransactionHistoryChild(val component: SavingsTransactionHistoryComponent): ChildHomeSign()
    }
}