package components.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import components.auth.AuthComponent
import components.countries.CountriesComponent
import components.rootLoans.RootLoansComponent
import components.onBoarding.OnBoardingComponent
import components.otp.OtpComponent
import components.rootBottomStack.RootBottomComponent
import components.rootSavings.RootSavingsComponent
import components.welcome.WelcomeComponent

interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>
    sealed class Child {
        class WelcomeChild(val component: WelcomeComponent) : Child()
        class OnboardingChild(val component: OnBoardingComponent) : Child()
        class CountriesChild(val component: CountriesComponent) : Child()
        class OTPChild(val component: OtpComponent) : Child()
        class AuthChild(val component: AuthComponent) : Child()
        class RootBottomChild(val component: RootBottomComponent) : Child()
        class RootLoansChild(val component: RootLoansComponent) : Child()

        class RootSavingsChild(val component: RootSavingsComponent) : Child()

    }
}