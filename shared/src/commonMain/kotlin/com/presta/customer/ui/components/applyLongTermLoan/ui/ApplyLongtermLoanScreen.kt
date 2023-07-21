package com.presta.customer.ui.components.applyLongTermLoan.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.applyLongTermLoan.ApplyLongTermLoanComponent

@Composable
fun ApplyLongTermLoanScreen(
    component: ApplyLongTermLoanComponent
) {
    val authState by component.authState.collectAsState()
    val  applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    ApplyLongTermLoansContent(
        component = component,
        state = applyLongTermLoansState
    )

}













