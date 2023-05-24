package com.presta.customer.ui.components.rootBottomStack

import ApplyLoanComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.profile.ProfileComponent
import com.presta.customer.ui.components.savings.SavingsComponent
import com.presta.customer.ui.components.sign.SignComponent

interface RootBottomComponent {

    val childStackBottom: Value<ChildStack<*, ChildBottom>>

    fun onProfileTabClicked()
    fun onLoanTabClicked()
    fun onSavingsTabClicked()
    fun onSignTabClicked()

    sealed class ChildBottom {
        class ProfileChild(val component: ProfileComponent) : ChildBottom()
        class ApplyLoanChild(val component: ApplyLoanComponent) : ChildBottom()
        class SavingsChild(val component: SavingsComponent) : ChildBottom()
        class SignChild(val component: SignComponent) : ChildBottom()
    }
}