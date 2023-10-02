package com.presta.customer.ui.components.signLoanForm.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.signLoanForm.SignLoanFormComponent

@Composable
fun SignLoanFormScreen (component: SignLoanFormComponent) {
    val applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    val authState by component.authState.collectAsState()

    SignLoanFormContent(
        component = component,
        state = applyLongTermLoansState,
        authState = authState,
        onEvent = component::onEvent,
        onDocumentSigned = component::onDocumentSigned,
        loanNumber = component.loanNumber,
        amount = component.amount
    )
}













