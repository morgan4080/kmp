package com.presta.customer.ui.components.registration.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.registration.RegistrationComponent

@Composable
fun RegistrationScreen(component: RegistrationComponent) {
    val authState by component.authState.collectAsState()
    val registrationState by component.state.collectAsState()

    RegistrationContent(
        state = registrationState,
        authState = authState,
        onEvent = component::onEvent,
        navigate = component::navigate
    )
}