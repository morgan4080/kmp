package com.presta.customer.ui.components.selectLoanPurpose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.selectLoanPurpose.SelectLoanPurposeComponent

@Composable
fun SelectLoanPurposeScreen(
    component: SelectLoanPurposeComponent
) {
    val authState by component.authState.collectAsState()
    val  applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    SelectLoanPurposeContent(
        component = component,
        state = applyLongTermLoansState,
        authState = authState,
        onEvent = component::onEvent
    )
}













