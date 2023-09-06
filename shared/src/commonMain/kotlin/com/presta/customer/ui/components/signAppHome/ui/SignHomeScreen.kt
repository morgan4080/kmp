package com.presta.customer.ui.components.signAppHome.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.signAppHome.SignHomeComponent

@Composable
fun SignHomeScreen(component: SignHomeComponent) {
    val authState by component.authState.collectAsState()
    val signHomeStateState by component.signHomeState.collectAsState()
    val applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    SignHomeContent(
        component = component,
        state = signHomeStateState,
        applyLongTermLoanState = applyLongTermLoansState,
        authState = authState,
        onApplyLongTermLoanEvent = component::onApplyLongTermLoanEvent
    )
}








