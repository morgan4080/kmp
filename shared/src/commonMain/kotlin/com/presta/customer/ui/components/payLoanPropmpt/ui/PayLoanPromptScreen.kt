package com.presta.customer.ui.components.payLoanPropmpt.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.payLoanPropmpt.PayLoanPromptComponent

@Composable
fun PayLoanPromptScreen(component: PayLoanPromptComponent){
    val authState by component.authState.collectAsState()
    val state by component.processingTransactionState.collectAsState()

    PayLoanPromptContent(
        authState,
        state,
        component.amount
    )
}