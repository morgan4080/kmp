package com.presta.customer.ui.components.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.auth.AuthComponent

@Composable
fun AuthScreen(component: AuthComponent) {
    val state by component.state.collectAsState()

    val onBoardingState by component.onBoardingState.collectAsState()
    val  tenantState by component.tenantState.collectAsState()

    AuthContent(
        state = state,
        onBoardingState = onBoardingState,
        onEvent = component::onEvent,
        onOnBoardingEvent = component::onOnBoardingEvent,
        navigate = component::navigate,
        platform = component.platform,
        reloadModels = component::reloadModels
    )
}