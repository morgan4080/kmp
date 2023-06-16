package com.presta.customer.ui.components.modeofDisbursement.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.modeofDisbursement.ModeOfDisbursementComponent

@Composable
fun SelectModeOfDisbursementScreen(
    component: ModeOfDisbursementComponent
) {

    val authState by component.authState.collectAsState()
    val  modeOfDisbursementState by component.modeOfDisbursementState.collectAsState()


    ModeOfDisbursementContent(
        component = component,
        authState = authState,
        state = modeOfDisbursementState,
        onEvent = component::onRequestLoanEvent,
        onAuthEvent = component::onAuthEvent,
    )

}
