import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.isEnter
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import components.root.RootComponent
import components.welcome.WelcomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRootUi(component: RootComponent) {

    val childStack by component.stack.subscribeAsState()
    val activeComponent = childStack.active.instance

    Children(
        stack = component.stack,
        animation = tabAnimation(),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.WelcomeChild -> WelcomeScreen(child.component)
            is RootComponent.Child.CountriesChild -> CountriesScreen(child.component)

            else -> {

            }
        }
    }

    /*Scaffold (
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
            ) { x ->
                when (val childX = x.instance) {
                    is RootComponent.Child.ProfileChild -> ProfileScreen(childX.component, innerPadding)
                    is RootComponent.Child.ApplyLoanChild -> ApplyLoanScreen(childX.component, innerPadding)
                    is RootComponent.Child.SavingsChild -> SavingsScreen(childX.component)
                    is RootComponent.Child.SignChild -> SignScreen(childX.component)
                    else -> {

                    }
                }
            }
        }
    )*/
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