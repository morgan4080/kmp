package components.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import components.auth.AuthComponent
import components.onBoarding.OnBoardingComponent
import components.otp.OtpComponent
import components.rootBottomStack.RootBottomComponent
import components.splash.SplashComponent
import components.welcome.WelcomeComponent

interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>
    sealed class Child {
        class SplashChild(val component: SplashComponent) : Child()
        class WelcomeChild(val component: WelcomeComponent) : Child()
        class OnboardingChild(val component: OnBoardingComponent) : Child()
        class OTPChild(val component: OtpComponent) : Child()
        class AuthChild(val component: AuthComponent) : Child()
        class RootBottomChild(val component: RootBottomComponent) : Child()
    }
}