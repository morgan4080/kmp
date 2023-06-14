package com.presta.customer.ui.components.processingTransaction.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.processingTransaction.ProcessingTransactionComponent

@Composable
fun ProcessingTransactionScreen(component: ProcessingTransactionComponent) {
    val authState by component.authState.collectAsState()
    val state by component.processingTransactionState.collectAsState()

    ProcessingTransactionContent(
        authState,
        state,
        component.amount,
        component::retryTransaction,
        component::navigateBack
    )
}