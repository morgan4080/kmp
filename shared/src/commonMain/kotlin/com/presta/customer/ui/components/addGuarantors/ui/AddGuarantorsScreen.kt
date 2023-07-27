package com.presta.customer.ui.components.addGuarantors.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.addGuarantors.AddGuarantorsComponent

@Composable
fun AddGuarantorsScreen(
    component: AddGuarantorsComponent
) {
    val authState by component.authState.collectAsState()
    val  applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    val  profileState by component.signHomeState.collectAsState()
    AddGuarantorContent(
        component = component,
        state = applyLongTermLoansState,
        authState = authState,
        onEvent = component::onEvent,
        signHomeState = profileState,
        onProfileEvent =component::onProfileEvent
    )



}















