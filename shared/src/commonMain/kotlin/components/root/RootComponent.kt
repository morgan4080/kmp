package components.root

import ApplyLoanComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import components.countries.CountriesComponent
import components.profile.ProfileComponent
import components.savings.SavingsComponent
import components.sign.SignComponent
import components.welcome.WelcomeComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onProfileTabClicked()
    fun onLoanTabClicked()
    fun onSavingsTabClicked()
    fun onSignTabClicked()
    // Defines all possible child components
    sealed class Child {
        class ProfileChild(val component: ProfileComponent) : Child()
        class ApplyLoanChild(val component: ApplyLoanComponent) : Child()
        class SavingsChild(val component: SavingsComponent) : Child()
        class SignChild(val component: SignComponent) : Child()
        class WelcomeChild(val component: WelcomeComponent) : Child()
        class CountriesChild(val component: CountriesComponent) : Child()



    }
}