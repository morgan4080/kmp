package components.root

import ApplyLoanComponent
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import components.applyLoan.DefaultApplyLoanComponent
import components.countries.CountriesComponent
import components.countries.DefaultCountriesComponent
import components.profile.DefaultProfileComponent
import components.profile.ProfileComponent
import components.root.RootComponent
import components.savings.DefaultSavingsComponent
import components.savings.SavingsComponent
import components.sign.DefaultSignComponent
import components.sign.SignComponent
import components.welcome.DefaultWelcomeComponent
import components.welcome.WelcomeComponent


class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val authenticated = true

    private val _stack =
        childStack(
            source = navigation,
            initialConfiguration = if (authenticated) Config.Profile else Config.Welcome,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override val stack: Value<ChildStack<*, RootComponent.Child>> = _stack

    private fun createChild(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            // reference children
            is Config.Welcome -> RootComponent.Child.WelcomeChild(welcomeComponent(componentContext))
            is Config.Countries -> RootComponent.Child.CountriesChild(countriesComponent(componentContext))
            is Config.Profile -> RootComponent.Child.ProfileChild(profileComponent(componentContext))
            is Config.ApplyLoan -> RootComponent.Child.ApplyLoanChild(applyLoanComponent(componentContext))
            is Config.Savings -> RootComponent.Child.SavingsChild(savingsComponent(componentContext))
            is Config.Sign -> RootComponent.Child.SignChild(signComponent(componentContext))
        }

    private fun welcomeComponent(componentContext: ComponentContext): WelcomeComponent =
        DefaultWelcomeComponent(
            componentContext = componentContext,
            onGetStartedSelected = {
                navigation.push(Config.Countries)
            },
        )

    private fun countriesComponent(componentContext: ComponentContext): CountriesComponent =
        DefaultCountriesComponent(
            componentContext = componentContext,
            onSelected = {

            },
        )

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
        navigation.bringToFront(Config.Profile)
    }

    override fun onLoanTabClicked() {
        navigation.bringToFront(Config.ApplyLoan)
    }

    override fun onSavingsTabClicked() {
        navigation.bringToFront(Config.Savings)
    }

    override fun onSignTabClicked() {
        navigation.bringToFront(Config.Sign)
    }


    private sealed interface Config : Parcelable {
        @Parcelize
        object Welcome : Config
        @Parcelize
        object Countries : Config
        @Parcelize
        object Profile : Config
        @Parcelize
        object ApplyLoan : Config
        @Parcelize
        object Savings : Config
        @Parcelize
        object Sign : Config
    }
}