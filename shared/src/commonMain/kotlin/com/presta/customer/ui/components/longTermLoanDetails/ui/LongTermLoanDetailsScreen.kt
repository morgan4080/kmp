package com.presta.customer.ui.components.longTermLoanDetails.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.longTermLoanDetails.LongTermLoanDetailsComponent

@Composable
fun LongTermLoanDetailsScreen(component: LongTermLoanDetailsComponent) {
    val  applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    LongTermLoanDetailsContent(
        component = component,
        state = applyLongTermLoansState
    )
}













