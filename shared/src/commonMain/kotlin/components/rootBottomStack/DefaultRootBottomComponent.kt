package components.rootBottomStack

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import components.payLoan.DefaultPayLoanComponent
import components.payLoan.PayLoanComponent
import components.payLoan.PayLoanScreen
import components.profile.DefaultProfileComponent
import components.profile.ProfileComponent
import components.rootLoans.DefaultRootLoansComponent
import components.rootLoans.RootLoansComponent
import components.rootSavings.DefaultRootSavingsComponent
import components.rootSavings.RootSavingsComponent
import components.sign.DefaultSignComponent
import components.sign.SignComponent


class DefaultRootBottomComponent(
    componentContext: ComponentContext,

) : RootBottomComponent, ComponentContext by componentContext {


    private val navigationBottomStackNavigation = StackNavigation<ConfigBottom>()
    private val navigationBottomStackNavigation2 = StackNavigation<RootLoansComponent.ChildLoans.PayLoanChild>()
    val component4=payLoanComponent(componentContext)

    private val _childStackBottom =
        childStack(
            source = navigationBottomStackNavigation,
            initialConfiguration = ConfigBottom.Profile,
            handleBackButton = true,
            childFactory = ::createChildBottom,
            key = "authStack"
        )

    override val childStackBottom: Value<ChildStack<*, RootBottomComponent.ChildBottom>> = _childStackBottom
    
    private fun createChildBottom(config: ConfigBottom, componentContext: ComponentContext): RootBottomComponent.ChildBottom =
        when (config) {
            is ConfigBottom.Profile -> RootBottomComponent.ChildBottom.ProfileChild(profileComponent(componentContext))
            is ConfigBottom.RootLoans -> RootBottomComponent.ChildBottom.RootLoansChild(rootLoansComponent(componentContext))
            is ConfigBottom.RootSavings -> RootBottomComponent.ChildBottom.RootSavingsChild(rootSavingsComponent(componentContext))
            is ConfigBottom.Sign -> RootBottomComponent.ChildBottom.SignChild(signComponent(componentContext))
        }

    private fun profileComponent(componentContext: ComponentContext): ProfileComponent =
        DefaultProfileComponent(
            componentContext = componentContext,
            onProfileClicked = {
                //Navigate to  User profile
            },
            onApplyLoanClicked = {
                //Navigate to loans Screen
                navigationBottomStackNavigation.bringToFront(ConfigBottom.RootLoans)

            },
            onAddSavingsClicked = {
            //Navigate to savings Screen
                navigationBottomStackNavigation.bringToFront(ConfigBottom.RootSavings)

            },
            onPayLoanClicked = {
                navigationBottomStackNavigation.bringToFront(ConfigBottom.RootLoans)

            },
            onViewFullStatementClicked = {
                //Navigate  to full Transaction History

            }

        )

    private fun rootLoansComponent(componentContext: ComponentContext): RootLoansComponent =
        DefaultRootLoansComponent(
            componentContext = componentContext,
        )

    private fun rootSavingsComponent(componentContext: ComponentContext): RootSavingsComponent =
        DefaultRootSavingsComponent(
            componentContext = componentContext,

        )
    private fun signComponent(componentContext: ComponentContext): SignComponent =
        DefaultSignComponent(
            componentContext = componentContext,
            onSelected = {

            }
        )

    private fun payLoanComponent(componentContext: ComponentContext): PayLoanComponent =
        DefaultPayLoanComponent(
            componentContext = componentContext,

            onPayClicked = {


            }
        )


    override fun onProfileTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Profile)
    }

    override fun onLoanTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.RootLoans)
    }

    override fun onSavingsTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.RootSavings)
    }

    override fun onSignTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Sign)
    }

    private sealed class ConfigBottom : Parcelable {
        @Parcelize
        object Profile : ConfigBottom()
        @Parcelize
        object RootLoans : ConfigBottom()
        @Parcelize
        object RootSavings : ConfigBottom()
        @Parcelize
        object Sign : ConfigBottom()

    }
}