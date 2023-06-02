package com.presta.customer.ui.components.specificLoanType.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.specificLoanType.SpecificLoansComponent

@Composable
fun SpecificLoansScreen(
    component: SpecificLoansComponent,
    innerPadding: PaddingValues

) {
    val authState by component.authState.collectAsState()
    val  specificLoanState by component.shortTermloansState.collectAsState()
    SpecificLoaContent(
        authState = authState,
        state = specificLoanState,
        onEvent = component::onEvent,
        onAuthEvent = component::onAuthEvent,
        innerPadding = innerPadding,
        component = component
    )
}
