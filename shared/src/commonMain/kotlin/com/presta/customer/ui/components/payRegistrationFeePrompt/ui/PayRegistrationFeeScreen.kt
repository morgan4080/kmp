package com.presta.customer.ui.components.payRegistrationFeePrompt.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.payRegistrationFeePrompt.PayRegistrationFeeComponent

@Composable
fun PayRegistrationFeeScreen(component: PayRegistrationFeeComponent) {
    val authState by component.authState.collectAsState()
    val state by component.processingTransactionState.collectAsState()
    PayRegistrationFeeContent(
        authState,
        state,
        component.amount
    )
}