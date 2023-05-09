package components.root

import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import components.countries.CountriesComponent
import components.countries.DefaultCountriesComponent
import components.profile.DefaultProfileComponent
import components.profile.ProfileComponent
import components.root.RootComponent
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
                // navigation.push(Config.Login)
            },
        )

    private fun profileComponent(componentContext: ComponentContext): ProfileComponent =
        DefaultProfileComponent(
            componentContext = componentContext,
            onLoanClicked = {
                // change to loans config
                navigation.push(Config.Welcome)
            }
        )

    private sealed interface Config : Parcelable {
        @Parcelize
        object Welcome : Config
        @Parcelize
        object Countries : Config
        @Parcelize
        object Profile : Config
    }
}