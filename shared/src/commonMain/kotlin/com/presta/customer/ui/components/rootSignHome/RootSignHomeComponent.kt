package com.presta.customer.ui.components.rootSignHome

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.addSavings.AddSavingsComponent
import com.presta.customer.ui.components.applyLongTermLoan.ApplyLongTermLoanComponent
import com.presta.customer.ui.components.longTermLoanDetails.LongTermLoanDetailsComponent
import com.presta.customer.ui.components.rootSavings.RootSavingsComponent
import com.presta.customer.ui.components.savingsTransactionHistory.SavingsTransactionHistoryComponent
import com.presta.customer.ui.signAppHome.SignHomeComponent

interface RootSignHomeComponent {
    val childSignHomeStack:Value<ChildStack<*, ChildHomeSign>>
    fun applyLongTermLoan()

    sealed class  ChildHomeSign {
        class SignHomeChild(val component: SignHomeComponent): ChildHomeSign()
        class TransactionHistoryChild(val component: SavingsTransactionHistoryComponent): ChildHomeSign()
//        class ApplyLongTermLoansChild(val component: ApplyLongTermLoanComponent): ChildHomeSign()
//        class LongTermLoanDetailsChild(val component: LongTermLoanDetailsComponent): ChildHomeSign()
    }
}