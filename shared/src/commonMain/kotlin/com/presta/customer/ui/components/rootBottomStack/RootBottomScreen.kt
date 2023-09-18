package com.presta.customer.ui.components.rootBottomStack

import GetIconForScreen
import RootSavingsScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.MR
import com.presta.customer.network.authDevice.model.PrestaServices
import com.presta.customer.network.authDevice.model.ServicesActivity
import com.presta.customer.network.authDevice.model.TenantServiceConfig
import com.presta.customer.network.authDevice.model.TenantServiceConfigResponse
import com.presta.customer.network.authDevice.model.TenantServicesResponse
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.profile.ui.ProfileScreen
import com.presta.customer.ui.components.rootLoans.RootLoansScreen
import com.presta.customer.ui.components.sign.SignScreen
import dev.icerock.moko.resources.compose.fontFamilyResource
@Composable
fun RootBottomScreen(component: RootBottomComponent) {
    val childStackBottom by component.childStackBottom.subscribeAsState()
    val state by component.authState.collectAsState()
    val activeComponentStackBottom = childStackBottom.active.instance
    // TenantServicesResponse
    val listOfActiveServices = remember { mutableListOf<TenantServicesResponse>() }
    
    val screens by remember { mutableStateOf(listOf(
        ScreensBottom("Home", component::onProfileTabClicked, activeComponentStackBottom is RootBottomComponent.ChildBottom.ProfileChild, PrestaServices.PRESTALENDER),
        ScreensBottom("Loans", component::onLoanTabClicked, activeComponentStackBottom is RootBottomComponent.ChildBottom.RootLoansChild, PrestaServices.PRESTALENDER),
        ScreensBottom("Savings", component::onSavingsTabClicked, activeComponentStackBottom is RootBottomComponent.ChildBottom.RootSavingsChild, PrestaServices.PRESTALENDER, TenantServiceConfig.savings),
        ScreensBottom("Sign", component::onSignTabClicked, activeComponentStackBottom is RootBottomComponent.ChildBottom.SignChild, PrestaServices.EGUARANTORSHIP),
    ))}
    // Filter screens list to exclude inactive features

    var savingsIsFalse by remember { mutableStateOf(false) }
    
    LaunchedEffect(state.tenantServicesConfig) {
        savingsIsFalse = state.tenantServicesConfig.contains(
            TenantServiceConfigResponse(TenantServiceConfig.savings, false)
        )
    }

    LaunchedEffect(state.tenantServices) {
        state.tenantServices.forEach { service ->
            if (service.status == ServicesActivity.ACTIVE) listOfActiveServices.add(service)
        }

        component.onAuthEvent(AuthStore.Intent.UpdateScreens(
            screens = screens.filter {
                listOfActiveServices.contains(TenantServicesResponse(it.serviceMapping, ServicesActivity.ACTIVE))
            }.filter { screen ->
                !(savingsIsFalse && screen.featureMapping == TenantServiceConfig.savings)
            }
        ))
    }

    LaunchedEffect(activeComponentStackBottom) {
        component.onAuthEvent(AuthStore.Intent.UpdateScreens(
            screens = state.screens.map { screen ->
                if (screen.name == "Home") screen.active = activeComponentStackBottom is RootBottomComponent.ChildBottom.ProfileChild
                if (screen.name == "Loans") screen.active = activeComponentStackBottom is RootBottomComponent.ChildBottom.RootLoansChild
                if (screen.name == "Savings") screen.active = activeComponentStackBottom is RootBottomComponent.ChildBottom.RootSavingsChild
                if (screen.name == "Sign") screen.active = activeComponentStackBottom is RootBottomComponent.ChildBottom.SignChild
                screen
            }
        ))
    }

    Scaffold (
        bottomBar = {
            AnimatedVisibility(
                visible = state.screens.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                BottomAppBar (
                    modifier = Modifier,
                    contentPadding = PaddingValues(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 25.dp),
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                    contentColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    state.screens.map { screen ->
                        NavigationBarItem (
                            icon = {
                                Icon(imageVector = GetIconForScreen(screen.name), contentDescription = null, modifier = Modifier.size(27.dp))
                            },
                            label = {
                                Text(
                                    text = screen.name,
                                    color= if (screen.active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                    modifier = Modifier.absoluteOffset(y = 30.dp)
                                )
                            },
                            selected = screen.active,
                            onClick = screen.component,
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.background, unselectedIconColor = MaterialTheme.colorScheme.outline)
                        )
                    }
                }
            }
        },
        content = {
            Children(
                stack = component.childStackBottom,
                animation = stackAnimation(fade() + scale()),
            ) {
                when (val childX = it.instance) {
                    is RootBottomComponent.ChildBottom.ProfileChild -> ProfileScreen(childX.component, callback = {
                        savingsIsFalse = state.tenantServicesConfig.contains(
                            TenantServiceConfigResponse(TenantServiceConfig.savings, false)
                        )

                        state.tenantServices.forEach { service ->
                            if (service.status == ServicesActivity.ACTIVE) listOfActiveServices.add(service)
                        }

                        component.onAuthEvent(AuthStore.Intent.UpdateScreens(
                            screens = screens.filter {
                                listOfActiveServices.contains(TenantServicesResponse(it.serviceMapping, ServicesActivity.ACTIVE))
                            }.filter { screen ->
                                !(savingsIsFalse && screen.featureMapping == TenantServiceConfig.savings)
                            }
                        ))
                    })
                    is RootBottomComponent.ChildBottom.RootLoansChild -> RootLoansScreen(childX.component)
                    is RootBottomComponent.ChildBottom.RootSavingsChild-> RootSavingsScreen(childX.component)
                    is RootBottomComponent.ChildBottom.SignChild -> SignScreen(childX.component)
                }
            }
        }
    )
}