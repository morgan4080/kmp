package com.presta.customer.ui.components.payLoan.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.payLoan.PayLoanComponent

@Composable
fun PayLoanScreen(component: PayLoanComponent){
    val profileState by component.profileState.collectAsState()

    PayLoanContent(
        component::onPaySelected,
        component::onBack,
        state = profileState
    )
}


























