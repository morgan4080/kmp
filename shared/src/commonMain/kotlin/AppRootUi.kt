import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.isEnter
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import components.applyLoan.ApplyLoanScreen
import components.profile.ProfileScreen
import components.root.RootComponent
import components.savings.SavingsScreen
import components.sign.SignScreen
import components.welcome.WelcomeScreen
import helpers.LocalSafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRootUi(component: RootComponent) {

    val childStack by component.stack.subscribeAsState()
    val activeComponent = childStack.active.instance

    Scaffold (
        modifier = Modifier.padding(LocalSafeArea.current),
        bottomBar = {
            val screens = listOf("Home", "Loans", "Savings", "Sign")

            BottomAppBar (
                contentPadding = PaddingValues(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 25.dp),
                containerColor = MaterialTheme.colorScheme.background
            ) {
                NavigationBarItem (
                    icon = { Icon(imageVector = GetIconForScreen(screens[0]), contentDescription = null, modifier = Modifier.size(27.dp)) },
                    label = {
                        Text(
                            text = screens[0],
                            color= if (activeComponent is RootComponent.Child.ProfileChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.absoluteOffset(y = 28.dp)
                        )
                    },
                    selected = activeComponent is RootComponent.Child.ProfileChild,
                    onClick = component::onProfileTabClicked,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.background, unselectedIconColor = MaterialTheme.colorScheme.outline)
                )
                NavigationBarItem (
                    icon = { Icon(imageVector = GetIconForScreen(screens[1]), contentDescription = null, modifier = Modifier.size(27.dp)) },
                    label = {
                        Text(
                            text = screens[1],
                            color= if (activeComponent is RootComponent.Child.ApplyLoanChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.absoluteOffset(y = 28.dp)
                        )
                    },
                    selected = activeComponent is RootComponent.Child.ApplyLoanChild,
                    onClick = component::onLoanTabClicked,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.background, unselectedIconColor = MaterialTheme.colorScheme.outline)
                )
                NavigationBarItem (
                    icon = { Icon(imageVector = GetIconForScreen(screens[2]), contentDescription = null, modifier = Modifier.size(27.dp)) },
                    label = {
                        Text(
                            text = screens[2],
                            color= if (activeComponent is RootComponent.Child.SavingsChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.absoluteOffset(y = 28.dp)
                        )
                    },
                    selected = activeComponent is RootComponent.Child.SavingsChild,
                    onClick = component::onSavingsTabClicked,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.background, unselectedIconColor = MaterialTheme.colorScheme.outline)
                )
                NavigationBarItem (
                    icon = { Icon(imageVector = GetIconForScreen(screens[3]), contentDescription = null, modifier = Modifier.size(27.dp)) },
                    label = {
                        Text(
                            text = screens[3],
                            color= if (activeComponent is RootComponent.Child.SignChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.absoluteOffset(y = 28.dp)
                        )
                    },
                    selected = activeComponent is RootComponent.Child.SignChild,
                    onClick = component::onSignTabClicked,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.background, unselectedIconColor = MaterialTheme.colorScheme.outline)
                )
            }
        },
        content = { innerPadding ->
            Children(
                stack = component.stack,
                animation = tabAnimation(),
            ) {
                when (val child = it.instance) {
                    is RootComponent.Child.WelcomeChild -> WelcomeScreen(child.component)
                    is RootComponent.Child.CountriesChild -> CountriesScreen(child.component)
                    is RootComponent.Child.ProfileChild -> ProfileScreen(child.component, innerPadding)
                    is RootComponent.Child.ApplyLoanChild -> ApplyLoanScreen(child.component, innerPadding)
                    is RootComponent.Child.SavingsChild -> SavingsScreen(child.component)
                    is RootComponent.Child.SignChild -> SignScreen(child.component)
                }
            }
        }
    )
}

@Composable
private fun tabAnimation(): StackAnimation<Any, RootComponent.Child> =
    stackAnimation { child, otherChild, direction ->
        val index = child.instance.index
        val otherIndex = otherChild.instance.index
        val anim = slide()
        if ((index > otherIndex) == direction.isEnter) anim else anim.flipSide()
    }

private fun StackAnimator.flipSide(): StackAnimator =
    StackAnimator { direction, isInitial, onFinished, content ->
        invoke(
            direction = direction.flipSide(),
            isInitial = isInitial,
            onFinished = onFinished,
            content = content,
        )
    }

private val RootComponent.Child.index: Int
    get() =
        when (this) {
            is RootComponent.Child.ProfileChild -> 0
            is RootComponent.Child.ApplyLoanChild -> 1
            is RootComponent.Child.SavingsChild  -> 2
            is RootComponent.Child.SignChild -> 3
            is RootComponent.Child.CountriesChild -> 4
            is RootComponent.Child.WelcomeChild -> 5
        }
@Suppress("OPT_IN_USAGE")
private fun Direction.flipSide(): Direction =
    when (this) {
        Direction.ENTER_FRONT -> Direction.ENTER_BACK
        Direction.EXIT_FRONT -> Direction.EXIT_BACK
        Direction.ENTER_BACK -> Direction.ENTER_FRONT
        Direction.EXIT_BACK -> Direction.EXIT_FRONT
    }