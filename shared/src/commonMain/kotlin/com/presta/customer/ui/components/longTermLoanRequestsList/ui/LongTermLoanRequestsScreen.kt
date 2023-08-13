package com.presta.customer.ui.components.longTermLoanRequestsList.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.longTermLoanRequestsList.LongTermLoanRequestsComponent

@Composable
fun LongTermLoanRequestsScreen(
    component: LongTermLoanRequestsComponent
) {
    val authState by component.authState.collectAsState()
    val applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    val profileState by component.signHomeState.collectAsState()
    LongTermLoanRequestsContent(
        component = component,
        state = applyLongTermLoansState,
        authState = authState,
        onEvent = component::onEvent,
        signHomeState = profileState
    )



}















