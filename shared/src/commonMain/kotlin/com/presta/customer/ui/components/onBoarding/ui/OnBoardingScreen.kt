package com.presta.customer.ui.components.onBoarding.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.onBoarding.OnBoardingComponent

@Composable
internal fun OnBoardingScreen(
    component: OnBoardingComponent
) {
    val onBoardingState by component.state.collectAsState()

    OnBoardingContent(
        state = onBoardingState,
        onEvent = component::onEvent,
        navigate = component::navigate
    )
}