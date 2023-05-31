package com.presta.customer.ui.components.rootBottomStack

import ApplyLoanComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.profile.ProfileComponent
import com.presta.customer.ui.components.rootSavings.RootSavingsComponent
import com.presta.customer.ui.components.savings.SavingsComponent
import com.presta.customer.ui.components.sign.SignComponent
import com.presta.customer.ui.components.transactionHistory.TransactionHistoryComponent
import components.rootLoans.RootLoansComponent

interface RootBottomComponent {

    val childStackBottom: Value<ChildStack<*, ChildBottom>>

    fun onProfileTabClicked()
    fun onLoanTabClicked()
    fun onSavingsTabClicked()
    fun onSignTabClicked()

    sealed class ChildBottom {
        class ProfileChild(val component: ProfileComponent) : ChildBottom()
        class RootLoansChild(val component: RootLoansComponent) : ChildBottom()
        class RootSavingsChild(val component: RootSavingsComponent) : ChildBottom()
        class SignChild(val component: SignComponent) : ChildBottom()
    }
}