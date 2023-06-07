package com.presta.customer.ui.components.topUp.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.topUp.LoanTopUpComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanTopUpScreen(
    component: LoanTopUpComponent
) {
    val authState by component.authState.collectAsState()
    val  shortTermLoansState by component.shortTermloansState.collectAsState()

    LoanTopUpContent(
        component=component,
        state = shortTermLoansState
    )


}
