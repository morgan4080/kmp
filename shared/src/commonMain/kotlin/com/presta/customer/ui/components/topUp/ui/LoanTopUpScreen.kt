package com.presta.customer.ui.components.topUp.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.topUp.LoanTopUpComponent

@Composable
fun LoanTopUpScreen(
    component: LoanTopUpComponent
) {
    val authState by component.authState.collectAsState()
    val  shortTermLoansState by component.shortTermloansState.collectAsState()
    val  profileState  by component.profileState.collectAsState()
    //pass profile  state and fetch the loan data

    LoanTopUpContent(
        component=component,
        state = shortTermLoansState,
        profileState=profileState
    )


}
