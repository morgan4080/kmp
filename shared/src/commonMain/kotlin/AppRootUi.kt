import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import components.root.RootComponent
import components.welcome.WelcomeScreen

@Composable
fun AppRootUi(component: RootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(fade() + scale()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.WelcomeChild -> WelcomeScreen(child.component)
            is RootComponent.Child.CountriesChild -> CountriesScreen(child.component)
        }
    }
}