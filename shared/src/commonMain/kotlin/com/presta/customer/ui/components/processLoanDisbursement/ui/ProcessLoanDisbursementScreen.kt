package com.presta.customer.ui.components.processLoanDisbursement.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.processLoanDisbursement.ProcessLoanDisbursementComponent

@Composable
fun ProcessLoanDisbursementScreen(
    component: ProcessLoanDisbursementComponent
) {
    val authState by component.authState.collectAsState()

    ProcessLoanDisbursementContent(
        amount = component.amount,
        fees = component.fees,
        phoneNumber = authState.cachedMemberData?.phoneNumber
    )
}