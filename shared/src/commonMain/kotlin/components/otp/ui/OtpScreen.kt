package components.otp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import components.otp.OtpComponent

@Composable
fun OtpScreen(component: OtpComponent) {
    val otpState by component.state.collectAsState()

    val authState by component.authState.collectAsState()

    val onBoardingState by component.onBoardingState.collectAsState()

    OtpContent(
        state = otpState,
        authState = authState,
        onBoardingState = onBoardingState,
        onEvent = component::onEvent,
        onAuthEvent = component::onAuthEvent,
        onOnBoardingEvent = component::onOnBoardingEvent,
        navigate = component::navigate
    )
}
