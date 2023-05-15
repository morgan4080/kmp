package components.rootBottomStack

import ApplyLoanComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import components.applyLoan.DefaultApplyLoanComponent
import components.profile.DefaultProfileComponent
import components.profile.ProfileComponent
import components.savings.DefaultSavingsComponent
import components.savings.SavingsComponent
import components.sign.DefaultSignComponent
import components.sign.SignComponent


class DefaultRootBottomComponent(
    componentContext: ComponentContext,
) : RootBottomComponent, ComponentContext by componentContext {

    private val navigationBottomStackNavigation = StackNavigation<ConfigBottom>()

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
            is ConfigBottom.ApplyLoan -> RootBottomComponent.ChildBottom.ApplyLoanChild(applyLoanComponent(componentContext))
            is ConfigBottom.Savings -> RootBottomComponent.ChildBottom.SavingsChild(savingsComponent(componentContext))
            is ConfigBottom.Sign -> RootBottomComponent.ChildBottom.SignChild(signComponent(componentContext))
        }

    private fun profileComponent(componentContext: ComponentContext): ProfileComponent =
        DefaultProfileComponent(
            componentContext = componentContext,
            onProfileClicked = {

            }
        )

    private fun applyLoanComponent(componentContext: ComponentContext): ApplyLoanComponent =
        DefaultApplyLoanComponent(
            componentContext = componentContext,
            onLoanClicked = {

            }
        )

    private fun savingsComponent(componentContext: ComponentContext): SavingsComponent =
        DefaultSavingsComponent(
            componentContext = componentContext,
            onSelected = {

            }
        )
    private fun signComponent(componentContext: ComponentContext): SignComponent =
        DefaultSignComponent(
            componentContext = componentContext,
            onSelected = {

            }
        )

    override fun onProfileTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Profile)
    }

    override fun onLoanTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.ApplyLoan)
    }

    override fun onSavingsTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Savings)
    }

    override fun onSignTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Sign)
    }

    private sealed class ConfigBottom : Parcelable {
        @Parcelize
        object Profile : ConfigBottom()
        @Parcelize
        object ApplyLoan : ConfigBottom()
        @Parcelize
        object Savings : ConfigBottom()
        @Parcelize
        object Sign : ConfigBottom()
    }
}