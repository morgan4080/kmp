package com.presta.customer.ui.components.rootBottomStack

import GetIconForScreen
import RootSavingsScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.MR
import com.presta.customer.ui.components.profile.ui.ProfileScreen
import com.presta.customer.ui.components.rootLoans.RootLoansScreen
import com.presta.customer.ui.components.sign.SignScreen
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun RootBottomScreen(component: RootBottomComponent) {
    val childStackBottom by component.childStackBottom.subscribeAsState()
    val activeComponentStackBottom = childStackBottom.active.instance

    Scaffold (
        bottomBar = {
            val screens = mapOf(
                "Home" to component::onProfileTabClicked,
                "Loans" to component::onLoanTabClicked,
                "Savings" to component::onSavingsTabClicked,
                "Sign" to component::onSignTabClicked
            )

            AnimatedVisibility(true) {
                BottomAppBar (
                    contentPadding = PaddingValues(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 25.dp),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    screens.map { screen ->
                        NavigationBarItem (
                            icon = {
                                Icon(imageVector = GetIconForScreen(screen.key), contentDescription = null, modifier = Modifier.size(27.dp))
                            },
                            label = {
                                Text(
                                    text = screen.key,
                                    color= if (activeComponentStackBottom is RootBottomComponent.ChildBottom.ProfileChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                    modifier = Modifier.absoluteOffset(y = 30.dp)
                                )
                            },
                            selected = activeComponentStackBottom is RootBottomComponent.ChildBottom.ProfileChild,
                            onClick = screen.value,
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.background, unselectedIconColor = MaterialTheme.colorScheme.outline)
                        )
                    }
                }
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