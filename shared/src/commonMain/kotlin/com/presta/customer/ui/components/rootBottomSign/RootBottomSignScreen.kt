package com.presta.customer.ui.components.rootBottomSign

import RootSignHomeScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
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
import com.presta.customer.ui.components.longTermLoanRequestsList.ui.LongTermLoanRequestsScreen
import com.presta.customer.ui.components.signAppSettings.ui.SignSettingsScreen
import com.presta.customer.ui.composables.GetIconForSignScreen
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource
@Composable
fun RootBottomSignScreen(component: RootBottomSignComponent) {
    val childStackBottom by component.childStackBottom.subscribeAsState()
    val activeComponentStackBottom = childStackBottom.active.instance
    //Todo----Modified going back to loan  requests when user hits resolve loan


    Scaffold(
        modifier = Modifier.padding(LocalSafeArea.current),
        bottomBar = {
            val screens = listOf("Home", "Request", "Setting", "Lms")

            AnimatedVisibility(true) {
                BottomAppBar(
                    contentPadding = PaddingValues(
                        start = 0.dp,
                        top = 0.dp,
                        end = 0.dp,
                        bottom = 25.dp
                    ),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = GetIconForSignScreen(screens[0]),
                                contentDescription = null,
                                modifier = Modifier.size(27.dp)
                            )
                        },
                        label = {
                            Text(
                                text = screens[0],
                                color = if (activeComponentStackBottom is RootBottomSignComponent.ChildBottom.ProfileChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                modifier = Modifier.absoluteOffset(y = 30.dp)
                            )
                        },
                        selected = activeComponentStackBottom is RootBottomSignComponent.ChildBottom.ProfileChild,
                        onClick = component::onProfileTabClicked,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.background,
                            unselectedIconColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = GetIconForSignScreen(screens[1]),
                                contentDescription = null,
                                modifier = Modifier.size(27.dp)
                            )
                        },
                        label = {
                            Text(
                                text = screens[1],
                                color = if (activeComponentStackBottom is RootBottomSignComponent.ChildBottom.RequestChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                modifier = Modifier.absoluteOffset(y = 30.dp)
                            )
                        },
                        selected = activeComponentStackBottom is RootBottomSignComponent.ChildBottom.RequestChild,
                        onClick = component::onRequestTabClicked,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.background,
                            unselectedIconColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = GetIconForSignScreen(screens[2]),
                                contentDescription = null,
                                modifier = Modifier.size(27.dp)
                            )
                        },
                        label = {
                            Text(
                                text = screens[2],
                                color = if (activeComponentStackBottom is RootBottomSignComponent.ChildBottom.SettingsChild) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                modifier = Modifier.absoluteOffset(y = 30.dp)
                            )
                        },
                        selected = activeComponentStackBottom is RootBottomSignComponent.ChildBottom.SettingsChild,
                        onClick = component::onSettingsTabClicked,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.background,
                            unselectedIconColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = GetIconForSignScreen(screens[3]),
                                contentDescription = null,
                                modifier = Modifier.size(27.dp)
                            )
                        },
                        label = {
                            Text(
                                text = screens[3],
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                modifier = Modifier.absoluteOffset(y = 30.dp)
                            )
                        },
                        selected = false,
                        onClick = component::onLmsTabClicked,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.background,
                            unselectedIconColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }
        },
        content = { innerPadding ->
            Children(
                stack = component.childStackBottom,
                animation = stackAnimation(fade() + scale()),
            ) {
                when (val childX = it.instance) {
                    is RootBottomSignComponent.ChildBottom.ProfileChild -> RootSignHomeScreen(childX.component)
                    is RootBottomSignComponent.ChildBottom.RequestChild -> LongTermLoanRequestsScreen(
                        childX.component
                    )
                    is RootBottomSignComponent.ChildBottom.SettingsChild -> SignSettingsScreen(
                        childX.component
                    )
                }
            }
        }
    )
}

private val RootBottomSignComponent.ChildBottom.index: Int
    get() =
        when (this) {
            is RootBottomSignComponent.ChildBottom.ProfileChild -> 0
            is RootBottomSignComponent.ChildBottom.RequestChild -> 1
            is RootBottomSignComponent.ChildBottom.SettingsChild -> 2
        }

private fun Direction.flipSide(): Direction =
    when (this) {
        Direction.ENTER_FRONT -> Direction.ENTER_BACK
        Direction.EXIT_FRONT -> Direction.EXIT_BACK
        Direction.ENTER_BACK -> Direction.ENTER_FRONT
        Direction.EXIT_BACK -> Direction.EXIT_FRONT
    }