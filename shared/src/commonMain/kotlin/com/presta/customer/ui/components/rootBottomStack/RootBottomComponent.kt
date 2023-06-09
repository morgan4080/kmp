package com.presta.customer.ui.components.rootBottomStack

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
<<<<<<< HEAD:shared/src/commonMain/kotlin/components/rootBottomStack/RootBottomComponent.kt
import components.rootLoans.RootLoansComponent
import components.profile.ProfileComponent
import components.rootSavings.RootSavingsComponent
import components.sign.SignComponent
=======
import com.presta.customer.ui.components.profile.ProfileComponent
import com.presta.customer.ui.components.rootSavings.RootSavingsComponent
import com.presta.customer.ui.components.sign.SignComponent
import com.presta.customer.ui.components.rootLoans.RootLoansComponent
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/components/rootBottomStack/RootBottomComponent.kt

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