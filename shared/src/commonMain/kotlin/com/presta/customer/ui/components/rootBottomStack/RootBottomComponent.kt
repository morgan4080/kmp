package com.presta.customer.ui.components.rootBottomStack

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.presta.customer.network.authDevice.model.PrestaServices
import com.presta.customer.network.authDevice.model.TenantServiceConfig
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.profile.ProfileComponent
import com.presta.customer.ui.components.rootLoans.RootLoansComponent
import com.presta.customer.ui.components.rootSavings.RootSavingsComponent
import com.presta.customer.ui.components.sign.SignComponent
import kotlinx.coroutines.flow.StateFlow

data class ScreensBottom(val name: String, val component: () -> Unit, var active: Boolean, val serviceMapping: PrestaServices, val featureMapping: TenantServiceConfig? = null)
interface RootBottomComponent {

    val childStackBottom: Value<ChildStack<*, ChildBottom>>
    fun onProfileTabClicked()
    fun onLoanTabClicked()
    fun onSavingsTabClicked()
    fun onSignTabClicked()

    val authStore: AuthStore

    val authState: StateFlow<AuthStore.State>

    fun onAuthEvent(event: AuthStore.Intent)

    sealed class ChildBottom {
        class ProfileChild(val component: ProfileComponent) : ChildBottom()
        class RootLoansChild(val component: RootLoansComponent) : ChildBottom()
        class RootSavingsChild(val component: RootSavingsComponent) : ChildBottom()
        class SignChild(val component: SignComponent) : ChildBottom()
    }
}