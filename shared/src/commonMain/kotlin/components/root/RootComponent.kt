package components.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import components.countries.CountriesComponent
import components.profile.ProfileComponent
import components.welcome.WelcomeComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    // Defines all possible child components
    sealed class Child {
        class WelcomeChild(val component: WelcomeComponent) : Child()
        class CountriesChild(val component: CountriesComponent) : Child()
        class ProfileChild(val component: ProfileComponent) : Child()
    }
}