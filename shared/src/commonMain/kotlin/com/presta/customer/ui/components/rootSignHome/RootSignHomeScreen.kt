import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.ui.components.rootSignHome.RootSignHomeComponent
import com.presta.customer.ui.components.signAppHome.ui.SignHomeScreen
import com.presta.customer.ui.helpers.LocalSafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootSignHomeScreen(component: RootSignHomeComponent) {
    val childSignHomeStack by component.childSignHomeStack.subscribeAsState()
    Scaffold (
        modifier = Modifier.padding(LocalSafeArea.current),
    ) { innerPadding ->
        Children(
            stack = component.childSignHomeStack,
            animation = stackAnimation(fade() + scale()),
        ) {
            when (val child = it.instance) {
                is RootSignHomeComponent.ChildHomeSign.SignHomeChild -> SignHomeScreen(child.component)
            }
        }
    }
}

