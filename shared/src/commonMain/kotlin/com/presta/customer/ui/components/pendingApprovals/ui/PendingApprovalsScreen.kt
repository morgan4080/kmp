package com.presta.customer.ui.components.pendingApprovals.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.pendingApprovals.LoanPendingApprovalsComponent

@Composable
fun PendingApprovalsScreen(
    component: LoanPendingApprovalsComponent
) {
    val authState by component.authState.collectAsState()
    val modeOfDisbursementState by component.modeOfDisbursementState.collectAsState()

    PendingApprovalsContent(
        authState = authState,
        modeOfDisbursementState = modeOfDisbursementState,
        onBack = component::onBackClicked,
        checkAuthenticatedUser = component::checkAuthenticatedUser,
    )
}