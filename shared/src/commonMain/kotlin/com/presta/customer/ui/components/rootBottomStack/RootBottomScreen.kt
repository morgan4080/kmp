package com.presta.customer.ui.components.rootBottomStack

import GetIconForScreen
import RootLoansScreen
import RootSavingsScreen
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
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.ui.components.profile.ui.ProfileScreen
import com.presta.customer.ui.components.sign.SignScreen
import com.presta.customer.ui.helpers.LocalSafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootBottomScreen(component: RootBottomComponent) {
    val childStackBottom by component.childStackBottom.subscribeAsState()
    val activeComponentStackBottom = childStackBottom.active.instance
    Scaffold (
        modifier = Modifier.padding(LocalSafeArea.current),
        bottomBar = {
            val screens = listOf("Home", "Loans", "Savings", "Sign")

            BottomAppBar (
                contentPadding = PaddingValues(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 25.dp),
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = Color.Transparent,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem (
                    icon = {
                        Icon(imageVector = GetIconForScreen(screens[0]), contentDescription = null, modifier = Modifier.size(27.dp))
                    },
                    label = {
                        Text(
                            text = screens[0],
                            color= if (activeComponentStackBottom is RootBottomComponent.ChildBottom.ProfileChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.absoluteOffset(y = 28.dp)
                        )
                    },
                    selected = activeComponentStackBottom is RootBottomComponent.ChildBottom.ProfileChild,
                    onClick = component::onProfileTabClicked,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.background, unselectedIconColor = MaterialTheme.colorScheme.outline)
                )
                NavigationBarItem (
                    icon = { Icon(imageVector = GetIconForScreen(screens[1]), contentDescription = null, modifier = Modifier.size(27.dp)) },
                    label = {
                        Text(
                            text = screens[1],
                            color= if (activeComponentStackBottom is RootBottomComponent.ChildBottom.RootLoansChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.absoluteOffset(y = 28.dp)
                        )
                    },
                    selected = activeComponentStackBottom is RootBottomComponent.ChildBottom.RootLoansChild,
                    onClick = component::onLoanTabClicked,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.background, unselectedIconColor = MaterialTheme.colorScheme.outline)
                )
                NavigationBarItem (
                    icon = { Icon(imageVector = GetIconForScreen(screens[2]), contentDescription = null, modifier = Modifier.size(27.dp)) },
                    label = {
                        Text(
                            text = screens[2],
                            color= if (activeComponentStackBottom is RootBottomComponent.ChildBottom.RootSavingsChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.absoluteOffset(y = 28.dp)
                        )
                    },
                    selected = activeComponentStackBottom is RootBottomComponent.ChildBottom.RootSavingsChild,
                    onClick = component::onSavingsTabClicked,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.background, unselectedIconColor = MaterialTheme.colorScheme.outline)
                )
                NavigationBarItem (
                    icon = { Icon(imageVector = GetIconForScreen(screens[3]), contentDescription = null, modifier = Modifier.size(27.dp)) },
                    label = {
                        Text(
                            text = screens[3],
                            color= if (activeComponentStackBottom is RootBottomComponent.ChildBottom.SignChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.absoluteOffset(y = 28.dp)
                        )
                    },
                    selected = activeComponentStackBottom is RootBottomComponent.ChildBottom.SignChild,
                    onClick = component::onSignTabClicked,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.background, unselectedIconColor = MaterialTheme.colorScheme.outline)
                )
            }
        },
        content = { innerPadding ->
            Children(
                stack = component.childStackBottom,
                animation = stackAnimation(fade() + scale()),
            ) {
                when (val childX = it.instance) {
                    is RootBottomComponent.ChildBottom.ProfileChild -> ProfileScreen(childX.component, innerPadding)
                    is RootBottomComponent.ChildBottom.RootLoansChild -> RootLoansScreen(childX.component)
                    is RootBottomComponent.ChildBottom.RootSavingsChild-> RootSavingsScreen(childX.component)
                    is RootBottomComponent.ChildBottom.SignChild -> SignScreen(childX.component)
                }
            }
        }
    )
}

private val RootBottomComponent.ChildBottom.index: Int
    get() =
        when (this) {
            is RootBottomComponent.ChildBottom.ProfileChild -> 0
            is RootBottomComponent.ChildBottom.RootLoansChild -> 1
            is RootBottomComponent.ChildBottom.RootSavingsChild  -> 2
            is RootBottomComponent.ChildBottom.SignChild -> 3
        }

@Suppress("OPT_IN_USAGE")
private fun Direction.flipSide(): Direction =
    when (this) {
        Direction.ENTER_FRONT -> Direction.ENTER_BACK
        Direction.EXIT_FRONT -> Direction.EXIT_BACK
        Direction.ENTER_BACK -> Direction.ENTER_FRONT
        Direction.EXIT_BACK -> Direction.EXIT_FRONT
    }
