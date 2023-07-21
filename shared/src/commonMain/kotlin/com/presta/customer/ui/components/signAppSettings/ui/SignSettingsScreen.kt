package com.presta.customer.ui.components.signAppSettings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.signAppSettings.SignSettingsComponent

@Composable
fun SignSettingsScreen(
    component: SignSettingsComponent,
    ) {
    val authState by component.authState.collectAsState()
    val  signHomeStateState by component.signHomeState.collectAsState()
    SignSettingsContent(
        component = component,
        state = signHomeStateState,
        authState = authState,
        onEvent = component::onEvent,
    )
}














