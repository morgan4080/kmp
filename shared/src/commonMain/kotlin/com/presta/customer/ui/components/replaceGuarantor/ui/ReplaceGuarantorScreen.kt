package com.presta.customer.ui.components.replaceGuarantor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.replaceGuarantor.ReplaceGuarantorComponent

@Composable
fun ReplaceGuarantorScreen(
    component: ReplaceGuarantorComponent
) {
    val authState by component.authState.collectAsState()
    val applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    val profileState by component.signHomeState.collectAsState()
    ReplaceGuarantorContent(
        component = component,
        state = applyLongTermLoansState,
        authState = authState,
        onEvent = component::onEvent,
        signHomeState = profileState,
        onProfileEvent = component::onProfileEvent,
    )

}


















