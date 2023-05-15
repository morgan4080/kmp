import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import components.countries.CountriesScreen
import components.onboarding.OnboardingScreen
import components.otp.OtpScreen
import components.root.RootComponent
import components.rootBottomStack.RootBottomScreen
import components.welcome.WelcomeScreen

@Composable
fun AppRootUi(component: RootComponent) {

    val childStack by component.childStack.subscribeAsState()

    Children(
        stack = component.childStack,
        animation = stackAnimation(fade() + scale()),// tabAnimation()
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.WelcomeChild -> WelcomeScreen(child.component)
            is RootComponent.Child.OnboardingChild -> OnboardingScreen(child.component)
            is RootComponent.Child.CountriesChild -> CountriesScreen(child.component)
            is RootComponent.Child.OTPChild -> OtpScreen(child.component)
            is RootComponent.Child.RootBottomChild -> RootBottomScreen(child.component)
        }
    }
}