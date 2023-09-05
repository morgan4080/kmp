package com.presta.customer.ui.components.signWitnessForm.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.presta.customer.ui.components.signGuarantorForm.SignGuarantorFormComponent

@Composable
fun SignWitnessFormScreen(component: SignGuarantorFormComponent) {
    val applyLongTermLoansState by component.applyLongTermLoansState.collectAsState()
    val authState by component.authState.collectAsState()
    SignWitnessFormContent(
        component = component,
        state = applyLongTermLoansState,
        authState = authState,
        onEvent = component::onEvent,
    )


}













