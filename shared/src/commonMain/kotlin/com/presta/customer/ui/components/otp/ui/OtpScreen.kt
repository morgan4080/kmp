package com.presta.customer.ui.components.otp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.otp.OtpComponent

@Composable
fun OtpScreen(component: OtpComponent) {
    val otpState by component.state.collectAsState()

    val authState by component.authState.collectAsState()

    OtpContent(
        state = otpState,
        authState = authState,
        onEvent = component::onEvent,
        navigate = component::navigate,
        platform = component.platform
    )
}
