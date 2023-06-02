package com.presta.customer.ui.components.loanConfirmation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.loanConfirmation.LoanConfirmationComponent

@Composable
fun LoansConfirmationScreen(
    component: LoanConfirmationComponent,
    innerPadding: PaddingValues

) {

    val authState by component.authState.collectAsState()
    val  loanConfirmationState by component.shortTermloansState.collectAsState()

    LoanConfirmationContent(
        authState = authState,
        state = loanConfirmationState,
        onEvent = component::onEvent,
        onAuthEvent = component::onAuthEvent,
        innerPadding = innerPadding,
        component = component

    )





}
