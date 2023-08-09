package com.presta.customer.ui.components.guarantorshipRequests.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.guarantorshipRequests.GuarantorshipRequestComponent

@Composable
fun GuarantorshipRequestScreen(component: GuarantorshipRequestComponent) {
    val applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()

    GuarantorShipRequestsContent(
        component = component,
        state = applyLongTermLoansState

    )

}














