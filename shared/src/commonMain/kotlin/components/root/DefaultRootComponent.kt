package components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import components.root.RootComponent
import components.welcome.DefaultWelcomeComponent
import components.welcome.WelcomeComponent


class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val _stack =
        childStack(
            source = navigation,
            initialConfiguration = Config.List,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override val stack: Value<ChildStack<*, RootComponent.Child>> = _stack

    private fun createChild(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.List -> RootComponent.Child.WelcomeChild(welcomeComponent(componentContext))
        }

    private fun welcomeComponent(componentContext: ComponentContext): WelcomeComponent =
        DefaultWelcomeComponent(
            componentContext = componentContext,
            onGetStartedSelected = {

            },
        )

    private sealed interface Config : Parcelable {
        @Parcelize
        object List : Config
    }
}