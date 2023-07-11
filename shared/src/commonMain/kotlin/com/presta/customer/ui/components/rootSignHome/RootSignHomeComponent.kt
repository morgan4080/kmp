package com.presta.customer.ui.components.rootSignHome

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.addSavings.AddSavingsComponent
import com.presta.customer.ui.components.applyLongTermLoan.ApplyLongTermLoanComponent
import com.presta.customer.ui.signAppHome.SignHomeComponent

interface RootSignHomeComponent {
    val childSignHomeStack:Value<ChildStack<*, ChildHomeSign>>

    sealed class  ChildHomeSign {
        class SignHomeChild(val component: SignHomeComponent): ChildHomeSign()
        class AddSavingsChild(val component: AddSavingsComponent): ChildHomeSign()
        class ApplyLongTermLoansChild(val component: ApplyLongTermLoanComponent): ChildHomeSign()
    }
}