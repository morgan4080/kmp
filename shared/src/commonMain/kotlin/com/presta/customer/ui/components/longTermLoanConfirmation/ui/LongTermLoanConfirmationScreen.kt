package com.presta.customer.ui.components.longTermLoanConfirmation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.longTermLoanConfirmation.LongTermLoanConfirmationComponent

@Composable
fun LongTermLoanConfirmationScreen(
    component: LongTermLoanConfirmationComponent
) {
    val authState by component.authState.collectAsState()
    val  signHomeStateState by component.signHomeState.collectAsState()
    LongTermLoanConfirmationContent(
        component = component,
        signProfileState = signHomeStateState
    )

}













