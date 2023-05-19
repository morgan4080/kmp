package components.rootBottomStack

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import components.rootLoans.RootLoansComponent
import components.profile.ProfileComponent
import components.rootSavings.RootSavingsComponent
import components.sign.SignComponent

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