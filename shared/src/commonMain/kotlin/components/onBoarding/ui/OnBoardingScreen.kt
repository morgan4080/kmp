package components.onBoarding.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import components.onBoarding.OnBoardingComponent

@Composable
internal fun OnBoardingScreen(
    component: OnBoardingComponent
) {
    val onBoardingState by component.state.collectAsState()

    val authState by component.authState.collectAsState()

    OnBoardingContent(
        state = onBoardingState,
        authState = authState,
        onEvent = component::onEvent
    )
}