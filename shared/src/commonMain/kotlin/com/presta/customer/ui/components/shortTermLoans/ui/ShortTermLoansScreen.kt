package com.presta.customer.ui.components.shortTermLoans.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.shortTermLoans.ShortTermLoansComponent

@Composable
fun ShortTermLoansScreen(
    component: ShortTermLoansComponent,
    innerPadding:PaddingValues){
    //short term loans Content
    val authState by component.authState.collectAsState()
    val  shortTermLoansState by component.shortTermloansState.collectAsState()

    ShortTermLoansContent(
        authState = authState,
        state = shortTermLoansState,
        onEvent = component::onEvent,
        onAuthEvent = component::onAuthEvent,
        innerPadding = innerPadding,
        component = component
    )
}