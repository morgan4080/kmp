package com.presta.customer.ui.components.rootBottomSign

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.rootSignHome.RootSignHomeComponent
import com.presta.customer.ui.components.longTermLoanRequestsList.LongTermLoanRequestsComponent
import com.presta.customer.ui.components.signAppSettings.SignSettingsComponent
import kotlinx.coroutines.flow.StateFlow

interface RootBottomSignComponent {

    val childStackBottom: Value<ChildStack<*, ChildBottom>>
    val loanRefId: String

    fun onProfileTabClicked()
    fun onRequestTabClicked()
    fun onSettingsTabClicked()
    fun onLmsTabClicked()

    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>

    fun onAuthEvent(event: AuthStore.Intent)

    sealed class ChildBottom {
        class ProfileChild(val component: RootSignHomeComponent) : ChildBottom()
        class RequestChild(val component: LongTermLoanRequestsComponent) : ChildBottom()
        class SettingsChild(val component: SignSettingsComponent) : ChildBottom()
    }


}