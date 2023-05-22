import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import components.auth.AuthScreen
import components.onBoarding.ui.OnBoardingScreen
import components.otp.ui.OtpScreen
import components.root.RootComponent
import components.rootBottomStack.RootBottomScreen
import components.splash.SplashScreen
import components.welcome.WelcomeScreen

@Composable
fun AppRootUi(component: RootComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade() + scale()),// tabAnimation()
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.SplashChild -> SplashScreen(child.component)
            is RootComponent.Child.WelcomeChild -> WelcomeScreen(child.component)
            is RootComponent.Child.OnboardingChild -> OnBoardingScreen(child.component)
            is RootComponent.Child.OTPChild -> OtpScreen(child.component)
            is RootComponent.Child.AuthChild -> AuthScreen(child.component)
            is RootComponent.Child.RootBottomChild -> RootBottomScreen(child.component)
        }
    }
}